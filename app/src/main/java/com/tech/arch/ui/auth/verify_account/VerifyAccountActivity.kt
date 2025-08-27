package com.tech.arch.ui.auth.verify_account

import android.content.Intent
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.tech.arch.data.model.ForgotPasswordApiResponse
import com.tech.arch.data.model.SignUpApiResponse
import com.tech.arch.databinding.ActivityVerifyAccountBinding
import com.tech.arch.ui.auth.reset_password.ResetPasswordActivity
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyAccountActivity : BaseActivity<ActivityVerifyAccountBinding>() {

    private val viewModel : VerifyAccountActivityVm by viewModels()
    private lateinit var otpETs: Array<EditText?>
    private lateinit var otpTimer: CountDownTimer
    private var email : String ? = null
    var isOtpComplete = false

    override fun getLayoutResource(): Int {
       return R.layout.activity_verify_account
    }

    override fun getViewModel(): BaseViewModel {
       return viewModel
    }

    override fun onCreateView() {
        ImageUtils.getStatusBarColor(this)
        initView()
        getData()
        initOnClick()
        setObserver()
    }

    private fun setObserver() {
        viewModel.obrCommon.observe(this, Observer {
            when(it?.status){
                Status.LOADING ->{
                    progressDialogAvl.isLoading(true)
                }
                Status.SUCCESS ->{
                    hideLoading()
                    when(it.message){
                        "otpVerify" ->{
                            val myDataModel : SignUpApiResponse? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                val intent = Intent(this , ResetPasswordActivity::class.java)
                                intent.putExtra("email",myDataModel.userExists?.email)
                                startActivity(intent)
                            }
                        }
                        "otpSend" ->{
                            val myDataModel : ForgotPasswordApiResponse ? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                showToast("Otp send successfully")
                            }
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

    private fun getData() {
        email = intent.getStringExtra("email")
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
                R.id.tvVerifyBtn ->{
                        val otpData =
                            "${binding.otpET1.text}" + "${binding.otpET2.text}" + "${binding.otpET3.text}" + "${binding.otpET4.text}"
                        if (otpData.isNotEmpty()) {
                            val data = HashMap<String, Any>()

                            data["email"] = email.toString()
                            data["otp"] = otpData
                            viewModel.otpVerify(data, Constants.VERIFY_OTP)

                        }else {
                            showToast("Please enter valid otp")
                        }

                }

                R.id.resendCode ->{
                    binding.otpET1.setText("")
                    binding.otpET2.setText("")
                    binding.otpET3.setText("")
                    binding.otpET4.setText("")

                    val data = HashMap<String, Any>()
                    data["email"] = email.toString()
                    data["type"] = 3

                    if (data != null){
                        viewModel.otpSend(data,Constants.FORGOT_PASSWORD)
                    }

                }
                R.id.iv_back ->{
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun initView() {
        otpETs = arrayOf(
            binding.otpET1, binding.otpET2, binding.otpET3, binding.otpET4
        )
        otpETs.forEachIndexed { index, editText ->
            editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int,
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (!s.isNullOrEmpty() && index != otpETs.size - 1) {
                        otpETs[index + 1]?.requestFocus()
                    }

                    // Check if all OTP fields are filled
                    isOtpComplete = otpETs.all { it!!.text.isNotEmpty() }

                }
            })

            editText?.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (editText.text.isEmpty() && index != 0) {
                        otpETs[index - 1]?.apply {
                            text?.clear()  // Clear the previous EditText before focusing
                            requestFocus()
                        }
                    }
                }
                // Check if all OTP fields are filled
                isOtpComplete = otpETs.all { it!!.text.isNotEmpty() }

                false
            }
        }
    }
}