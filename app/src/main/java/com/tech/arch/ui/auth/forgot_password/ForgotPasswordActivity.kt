package com.tech.arch.ui.auth.forgot_password

import android.content.Intent
import android.text.TextUtils
import android.util.Patterns
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.tech.arch.data.model.ForgotPasswordApiResponse
import com.tech.arch.databinding.ActivityForgotPasswordBinding
import com.tech.arch.ui.auth.verify_account.VerifyAccountActivity
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>() {


    private val viewModel: ForgotPasswordActivityVm by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.activity_forgot_password
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
        viewModel.obrCommon.observe(this , Observer {
            when(it?.status){
                Status.LOADING ->{
                    progressDialogAvl.isLoading(true)
                }
                Status.SUCCESS ->{
                    hideLoading()
                    val myDataModel : ForgotPasswordApiResponse ? = ImageUtils.parseJson(it.data.toString())
                    if (myDataModel != null){
                        showToast("Otp send successfully")
                        val intent = Intent(this , VerifyAccountActivity::class.java)
                        intent.putExtra("email",myDataModel.userExists?.email)
                        startActivity(intent)
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
        viewModel.onClick.observe(this, Observer {
            when (it?.id) {
                R.id.iv_back -> {
                    onBackPressedDispatcher.onBackPressed()
                }

                R.id.tvContinueBtn -> {
                    if (isEmptyField()){
                        if (Patterns.EMAIL_ADDRESS.matcher(binding.tvEmail.text.toString().trim()).matches()){
                            val data = HashMap<String, Any>()
                            data["email"] = binding.tvEmail.text.toString().trim()
                            data["type"] = 3

                            viewModel.forgotPassword(data, Constants.FORGOT_PASSWORD)
                        }else{
                            showToast("Invalid email")
                        }
                    }
                }
            }
        })
    }

    private fun isEmptyField(): Boolean {

        if (TextUtils.isEmpty(binding.tvEmail.text.toString().trim())) {
            showToast("Please enter Email")
            return false
        }
        return true
    }
}