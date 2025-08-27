package com.tech.arch.ui.auth.reset_password

import android.content.Intent
import android.text.InputType
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.tech.arch.data.api.SimpleApiResponse
import com.tech.arch.databinding.ActivityResetPasswordBinding
import com.tech.arch.ui.auth.login_module.LoginActivity
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding>() {

    private val viewModel : ResetPasswordActivityVm by viewModels()
    private var email : String ?= null
    override fun getLayoutResource(): Int {
      return R.layout.activity_reset_password
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        ImageUtils.getStatusBarColor(this)
        initOnClick()
        getData()
        setObserver()
    }

    private fun setObserver() {
        viewModel.obrCommon.observe(this , Observer {
            when(it?.status){
                Status.LOADING ->{
                    progressDialogAvl.isLoading(true)
                }
                Status.SUCCESS ->{
                    hideLoading()
                    val myDataModel : SimpleApiResponse ?= ImageUtils.parseJson(it.data.toString())
                    if (myDataModel != null){
                        showToast("Password reset successfully")
                        val intent = Intent(this , LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                Status.ERROR ->{
                    hideLoading()
                    showToast(it.message.toString())
                }
                else ->{

                }
            }
        })
    }

    private fun getData() {
        email = intent.getStringExtra("email")
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
                R.id.iv_back ->{
                    val intent = Intent(this , LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.showPassword -> {
                    showHideNewPassword()
                }
                R.id.showConfirmPassword ->{
                    showHideConfirmPassword()
                }
                R.id.tvConfirmBtn->{
                    if(isEmptyField()){
                        val data = HashMap<String, Any>()
                        data["email"] = email.toString()
                        data["newPassword"] = binding.etNewPassword.text.toString().trim()

                        viewModel.resetPassword(data,Constants.RESET_PASSWORD)
                    }


                }

            }
        })
    }

    private fun showHideNewPassword() {
        // Save the current typeface
        val typeface = binding.etNewPassword.typeface
        if (binding.etNewPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            binding.showPassword.setImageResource(R.drawable.iv_show_password)
            binding.etNewPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            binding.showPassword.setImageResource(R.drawable.iv_hide_password)
            binding.etNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }


        // Reapply the saved typeface to maintain the font style
        binding.etNewPassword.typeface = typeface
        binding.etNewPassword.setSelection(binding.etNewPassword.length())
    }


    private fun showHideConfirmPassword() {
        // Save the current typeface
        val typeface = binding.etConfirmPass.typeface
        if (binding.etConfirmPass.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            binding.showConfirmPassword.setImageResource(R.drawable.iv_show_password)
            binding.etConfirmPass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            binding.showConfirmPassword.setImageResource(R.drawable.iv_hide_password)
            binding.etConfirmPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }


        // Reapply the saved typeface to maintain the font style
        binding.etConfirmPass.typeface = typeface
        binding.etConfirmPass.setSelection(binding.etConfirmPass.length())
    }

    private fun isEmptyField(): Boolean {
        val password = binding.etNewPassword.text.toString().trim()

        if (TextUtils.isEmpty(binding.etNewPassword.text.toString().trim())){
            showToast("Please enter new password")
            return false
        }
        if (TextUtils.isEmpty(binding.etConfirmPass.text.toString().trim())){
            showToast("Please enter confirm password")
            return  false
        }
        if (password != binding.etConfirmPass.text.toString().trim()){
            showToast("New password  and confirm password not matched")
            return false
        }
        if (password.length < 8 && password.length > 20){
            showToast("Password length should be minimum 8 and maximum 20 ")
            return false
        }
        return true
    }
}