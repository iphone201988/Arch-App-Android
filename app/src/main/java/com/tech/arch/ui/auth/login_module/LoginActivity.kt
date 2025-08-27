package com.tech.arch.ui.auth.login_module

import android.annotation.SuppressLint
import android.content.Intent
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.tech.arch.data.model.SignUpApiResponse
import com.tech.arch.databinding.ActivityLoginBinding
import com.tech.arch.ui.auth.create_account.CreateAccountActivity
import com.tech.arch.ui.auth.forgot_password.ForgotPasswordActivity
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.home_screens.HomeDashBoardActivity
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel : LoginActivityVm by  viewModels()
    var token = "123"
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth
    companion object {
        var lat = 0.0
        var long = 0.0
    }
    override fun getLayoutResource(): Int {
         return  R.layout.activity_login
    }

    override fun getViewModel(): BaseViewModel {
     return  viewModel
    }

    override fun onCreateView() {

        ImageUtils.getStatusBarColor(this)
       initView()
       initOnClick()
       setObserver()

        initGoogleSignIn()

    }



    private fun loginWithTwitter() {
        val provider = OAuthProvider.newBuilder("twitter.com")

        val pendingResultTask = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener {

                    val user = it.user
                    Toast.makeText(this, "Welcome back, ${user?.displayName}", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            firebaseAuth.startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener {
                    val user = it.user
                    Toast.makeText(this, "Logged in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    Log.d("TwitterLogin", "User: ${user?.uid}, Name: ${user?.displayName}")
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_LONG).show()
                    Log.e("TwitterLogin", "Error: ", it)
                }
        }
    }


    private fun initGoogleSignIn() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

    }

    @SuppressLint("SuspiciousIndentation")
    private fun signInGoogle() {
        mGoogleSignInClient.signOut()
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleTask(task)
        }
    }


    private fun handleTask(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            if (account != null) {
                val userMap =  HashMap<String, Any>()
                userMap["email"] = account.email.toString()
                userMap["socialId"] = account.id.toString()
                userMap["socialType"] = 1
                userMap["deviceToken"] = token.toString()
                userMap["timeZone"] = Constants.timeZone
                userMap["deviceType"] = 1
                userMap["lat"] = lat.toString()
                userMap["lng"] = long.toString()
                viewModel.loginAccount(userMap,Constants.SOCIAL_LOGIN)
            }
        } catch (e: ApiException) {
            // Log the status code for more detailed debugging
            Log.e("GoogleSignIn", "signInResult:failed code=" + e.statusCode)
            showToast("Sign-In Failed: " + e.message)
        }
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
                        "loginAccount" ->{
                            val myDataModel : SignUpApiResponse? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                sharedPrefManager.saveUser(myDataModel)
                                sharedPrefManager.saveToken(myDataModel.userExists?.token.toString())
                                val intent = Intent(this , HomeDashBoardActivity::class.java)
                                startActivity(intent)
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

    private fun initView() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            }
            token = it.result
            Log.i("daadd", "initView: $token")
        }
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
                R.id.tvLoginBtn ->{
                    if (isEmptyField()){
                        if (Patterns.EMAIL_ADDRESS.matcher(binding.tvEmail.text.toString().trim()).matches()){
                            val data = HashMap<String,Any>()
                            data["email"] = binding.tvEmail.text.toString().lowercase().trim()
                            data["password"] = binding.etPassword.text.toString().trim()
                            data["deviceType"] = 1
                            data["deviceToken"] = token
                            data["lat"] = lat.toString()
                            data["lng"] = long.toString()
                            Log.i("fsdfds", "initOnClick: $data")
                            if (data != null){
                                viewModel.loginAccount(data, Constants.LOGIN)
                            }
                        }else {
                            showToast("Invalid email")
                        }
                    }
                }
                R.id.tvLoginBtn ->{
                    val intent = Intent(this , HomeDashBoardActivity::class.java)
                               startActivity(intent)
                }
                R.id.signUp ->{
                    val intent = Intent(this, CreateAccountActivity:: class.java)
                    startActivity(intent)
                }
                R.id.showPassword ->{
                    showHideNewPassword()
                }
                R.id.tvForgotPassword ->{
                    val intent = Intent(this , ForgotPasswordActivity ::class.java)
                    startActivity(intent)
                }
//                R.id.tvGoogleLogin ->{
//                    signInGoogle()
//                }
//                R.id.tvTwitterLogin ->{
//                    loginWithTwitter()
//                }

            }
        })

    }

    private fun showHideNewPassword() {
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

    private fun isEmptyField(): Boolean {
        if (TextUtils.isEmpty(binding.tvEmail.text.toString().trim())) {
            showToast("Please enter Email")
            return false
        }
        if (TextUtils.isEmpty(binding.etPassword.text.toString().trim())) {
            showToast("Please enter password")
            return false
        }
        return true
    }

}