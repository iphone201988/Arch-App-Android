package com.tech.arch.ui.auth.create_account

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.tech.arch.R
import com.tech.arch.databinding.ActivityCreateAccountBinding
import com.tech.arch.ui.auth.login_module.LoginActivity
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.ImageUtils
import com.google.firebase.messaging.FirebaseMessaging
import com.tech.arch.BR
import com.tech.arch.data.api.Constants
import com.tech.arch.data.model.AccountType
import com.tech.arch.data.model.SignUpApiResponse
import com.tech.arch.databinding.ItemLayoutAccountTypeBinding
import com.tech.arch.ui.base.SimpleRecyclerViewAdapter
import com.tech.arch.ui.base.permission.PermissionHandler
import com.tech.arch.ui.base.permission.Permissions
import com.tech.arch.ui.home_screens.HomeDashBoardActivity
import com.tech.arch.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CreateAccountActivity : BaseActivity<ActivityCreateAccountBinding>() {

    private val viewModel : CreateAccountActivityVm by viewModels()
    private lateinit var accountAdapter: SimpleRecyclerViewAdapter<AccountType, ItemLayoutAccountTypeBinding>
    private var accountList = ArrayList<AccountType>()
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    var token = "123"

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth


    private var photoFile: File? = null
    private var photoURI: Uri? = null
    private var imageUri : Uri? = null

    companion object {
        var lat = 0.0
        var long = 0.0
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_create_account
    }

    override fun getViewModel(): BaseViewModel {
       return viewModel
    }

    override fun onCreateView() {
        ImageUtils.getStatusBarColor(this)

        initView()
        initOnClick()
        galleryLauncher()
        getAccountList()
        initAdapter()
        setObserver()

        initGoogleSignIn()

    }

    private fun getAccountList() {
        accountList.add(AccountType("Individual User"))
        accountList.add(AccountType("Group"))
        accountList.add(AccountType("Business"))
    }

    private fun initAdapter() {
        accountAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_account_type,BR.bean){v,m,pos ->
            when(v.id){
                R.id.consMain ->{
                    binding.tvAccountType.setText(m.account)
                    Constants.accountType = m.account
                    binding.rvAccountType.visibility = View.GONE
                }
            }

        }
        binding.rvAccountType.adapter = accountAdapter
        accountAdapter.list = accountList
        accountAdapter.notifyDataSetChanged()

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
                userMap["deviceType"] = 1
                userMap["timeZone"] = Constants.timeZone
                userMap["lat"] = LoginActivity.lat.toString()
                userMap["lng"] = LoginActivity.long.toString()
           //     viewModel.createAccount(userMap, Constants.SOCIAL_LOGIN)
            }
        } catch (e: ApiException) {
            // Log the status code for more detailed debugging
            Log.e("GoogleSignIn", "signInResult:failed code=" + e.statusCode)
            showToast("Sign-In Failed: " + e.message)
        }
    }


    private fun setObserver() {
        viewModel.obrCommon.observe(this , Observer {
            when(it?.status){
                Status.LOADING -> {
                    progressDialogAvl.isLoading(true)
                }
                Status.SUCCESS ->{
                    hideLoading()
                    when(it.message) {
                        "createAccount" ->{
                            Log.i("sdsad", "setObserver: ${it.data}")
                             val myDataModel  : SignUpApiResponse? = ImageUtils.parseJson(it.data.toString())
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
            Constants.deviceToken = it.result
            Log.i("daadd", "initView: $token")
        }
    }
    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
               R.id.tvSignUpBtn -> {
                   if (isEmptyField()){
                       RegistrationDataHolder.email =  binding.tvEmail.text.toString().trim()
                       RegistrationDataHolder.password  = binding.etPassword.text.toString().trim()
                       RegistrationDataHolder.accountType = binding.tvAccountType.text.toString().trim()
                       RegistrationDataHolder.fullName = binding.tvName.text.toString().trim()
                       RegistrationDataHolder.phone = binding.tvOtpVerification.text.toString().trim()
                       RegistrationDataHolder.location = binding.tvLocationName.text.toString().trim()

                       val intent = Intent(this , AccountSecondStepActivity ::class.java)
                       startActivity(intent)
                   }

//                    if (isEmptyField()){
//                        if (Patterns.EMAIL_ADDRESS.matcher(binding.tvEmail.text.toString().trim()).matches()){
//                            val data = HashMap<String,Any>()
//                            data["email"] = binding.tvEmail.text.toString().lowercase().trim()
//                            data["password"] = binding.etPassword.text.toString().trim()
//                            data["deviceType"] = 1
//                            data["deviceToken"] = token
//                           // data["fullName"] = binding.etName.text.toString().trim()
//                            data["lat"] = lat.toString()
//                            data["lng"] = long.toString()
//                            if (data != null){
//                                viewModel.createAccount(data,Constants.SIGNUP)
//                            }
//                        }else {
//                            showToast("Invalid email")
//                        }
//
//                    }
//
                }

                R.id.iv_camera ->{
                    openCamera()

                }
                R.id.iv_gallery ->{
                    if (!ImageUtils.hasPermissions(
                            this,
                            ImageUtils.permissions
                        )
                    ) {
                        permissionResultLauncher.launch(ImageUtils.permissions)
                    } else {
                        selectImage()
                    }
                }

                R.id.tvAccountType ->{
                    binding.rvAccountType.visibility = View.VISIBLE
                }
                R.id.showPassword ->{
                    showHideNewPassword()
                }
//                R.id.showConfirmPassword ->{
//                    showHideConfirmPassword()
//                }
//                R.id.iv_back ->{
//                    onBackPressedDispatcher.onBackPressed()
//                }




//                R.id.signIn ->{
//                    val intent = Intent(this , LoginActivity::class.java)
//                    startActivity(intent)
//                }


//                R.id.tvGoogleLogin ->{
//                    signInGoogle()
//                }
            }
        })
    }

    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Permissions.check(this, Manifest.permission.CAMERA, 0, object : PermissionHandler() {
                override fun onGranted() {
                    openCameraIntent()
                }
            })
        } else {
            openCameraIntent()
        }
    }


    private fun openCameraIntent() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = ImageUtils.createImageFile(this)
        val authority = "${this.packageName}.fileProvider"
        val photoURI = FileProvider.getUriForFile(this, authority, photoFile!!)

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        cameraLauncher.launch(cameraIntent)

    }

    private var cameraLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    photoURI = photoFile!!.absoluteFile.toUri()
                    if (photoURI != null) {
                        photoURI?.let { uri ->
                           binding.ivProfile.setImageURI(photoURI)
                            RegistrationDataHolder.profileImage= photoURI
                        }
                        Log.i("ImageUpload", "cameraLauncher: $photoURI")
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }




    private fun galleryLauncher() {
        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    imageUri = data?.data
                    if (imageUri != null) {

                        imageUri?.let { uri ->
                         binding.ivProfile.setImageURI(imageUri)
                            RegistrationDataHolder.profileImage= imageUri

                        }
                        Log.i("ImageUpload", "galleryLauncher: $imageUri")
                    }
                }
            }
    }


    private var allGranted = false
    private val permissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            for (it in permissions.entries) {
                it.key
                val isGranted = it.value
                allGranted = isGranted
            }
            when {
                allGranted -> {
                    selectImage()
                }

            }
        }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        pickImageLauncher.launch(Intent.createChooser(intent, "Select Picture"))
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





//
//    private fun showHideConfirmPassword() {
//        // Save the current typeface
//        val typeface = binding.etConfirmPass.typeface
//        if (binding.etConfirmPass.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
//            binding.showConfirmPassword.setImageResource(R.drawable.iv_show_password)
//            binding.etConfirmPass.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
//        } else {
//            binding.showConfirmPassword.setImageResource(R.drawable.iv_hide_password)
//            binding.etConfirmPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
//        }
//
//
//        // Reapply the saved typeface to maintain the font style
//        binding.etConfirmPass.typeface = typeface
//        binding.etConfirmPass.setSelection(binding.etPassword.length())
//    }
//




    private fun isEmptyField(): Boolean {
        val password = binding.etPassword.text.toString().trim()


        if (TextUtils.isEmpty(binding.tvName.text.toString().trim())) {
            showToast("Please enter your name ")
            return false
        }

        if (TextUtils.isEmpty(binding.tvEmail.text.toString().trim())) {
            showToast("Please enter Email")
            return false
        }
        if (TextUtils.isEmpty(binding.etPassword.text.toString().trim())) {
            showToast("Please enter password")
            return false
        }
        if (TextUtils.isEmpty(binding.tvOtpVerification.text.toString().trim())) {
            showToast("Please enter number")
            return false
        }
        if (TextUtils.isEmpty(binding.tvAccountType.text.toString().trim())) {
            showToast("Please select account type")
            return false
        }
//        if (TextUtils.isEmpty(binding.etConfirmPass.text.toString().trim())){
//            showToast("Please enter confirm password")
//            return false
//        }
//        if (TextUtils.isEmpty(binding.etName.text.toString().trim())){
//            showToast("Please enter your name")
//            return false
//        }
//        if (TextUtils.isEmpty(binding.etConfirmPass.text.toString().trim())){
//            showToast("Please enter confirm password")
//            return  false
//        }
//        if (password != binding.etConfirmPass.text.toString().trim()){
//            showToast("Password and confirm password not matched")
//            return false
//        }
        if (password.length < 8 && password.length > 20){
            showToast("Password length should be minimum 8 and maximum 20 ")
            return false
        }
        return true
    }
}