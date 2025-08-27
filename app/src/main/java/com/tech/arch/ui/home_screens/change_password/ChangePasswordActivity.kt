package com.tech.arch.ui.home_screens.change_password

import android.text.InputType
import android.text.TextUtils
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.tech.arch.data.model.GetProfileApiResponse
import com.tech.arch.databinding.ActivityChangePasswordBinding
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {

    private val viewModel : ChangePasswordActivityVm by viewModels()


    override fun getLayoutResource(): Int {
       return R.layout.activity_change_password
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        ImageUtils.getStatusBarColor(this)
      initOnClick()
        setObserver()
    }

    private fun setObserver() {
        viewModel.obrCommom.observe(this , Observer {
            when(it?.status){
                Status.LOADING ->{
                    progressDialogAvl.isLoading(true)
                }
                Status.SUCCESS ->{
                    hideLoading()
                    val myDataModel : GetProfileApiResponse ? = ImageUtils.parseJson(it.data.toString())
                    if (myDataModel != null){
                        if (myDataModel.userExists != null){
                            showToast("Password changed successfully")
                        onBackPressedDispatcher.onBackPressed()
                        }
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

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
                R.id.iv_back ->{
                    onBackPressedDispatcher.onBackPressed()
                }
                R.id.showPassword ->{
                    showHidePassword()
                }
                R.id.showNewPassword ->{
                    showHideNewPassword()
                }
                R.id.showConfirmPassword ->{
                    showHideConfirmPassword()
                }
                R.id.tvConfirmBtn ->{
                    if (isEmptyField()){
                       val data = HashMap<String,RequestBody>()
                        data["oldPassword"] = getRequestBody(binding.etPassword.text.toString().trim())
                        data["newPassword"] = getRequestBody(binding.etNewPassword.text.toString().trim())

                        viewModel.changePassword(Constants.CHANGE_PASSWORD,null,data)
                    }
                }
            }
        })
    }


    private fun showHidePassword() {
        // Save the current typeface
        val typeface = binding.etPassword.typeface
        if (binding.etPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            binding.showPassword.setImageResource(R.drawable.iv_show_password)
            binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            binding.showPassword.setImageResource(R.drawable.iv_hide_password)
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }


        // Reapply the saved typeface to maintain the font style
        binding.etPassword.typeface = typeface
        binding.etPassword.setSelection(binding.etPassword.length())
    }

    private fun showHideNewPassword() {
        // Save the current typeface
        val typeface = binding.etNewPassword.typeface
        if (binding.etNewPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            binding.showNewPassword.setImageResource(R.drawable.iv_show_password)
            binding.etNewPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            binding.showNewPassword.setImageResource(R.drawable.iv_hide_password)
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

        if (TextUtils.isEmpty(binding.etPassword.text.toString().trim())) {
            showToast("Please enter old password")
            return false
        }
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

    fun getRequestBody(text: String): okhttp3.RequestBody {
        return text.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}