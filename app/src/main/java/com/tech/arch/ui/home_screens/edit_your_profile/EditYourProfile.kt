package com.tech.arch.ui.home_screens.edit_your_profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tech.arch.R
import com.tech.arch.data.model.GetProfileApiResponse
import com.tech.arch.databinding.ActivityEditYourProfileBinding
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.Status
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUtil
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class EditYourProfile : BaseActivity<ActivityEditYourProfileBinding>() {

    private var imageUri: Uri? = null

    private val viewModel : EditYourProfileVm by viewModels()
    override fun getLayoutResource(): Int {
       return R.layout.activity_edit_your_profile
    }

    override fun getViewModel(): BaseViewModel {
       return viewModel
    }

    override fun onCreateView() {
        ImageUtils.getStatusBarColor(this )
        initOnClick()
        viewModel.getProfile(com.tech.arch.data.api.Constants.GET_PROFILE)
        setObserver()
    }

    private fun setObserver() {
         viewModel.obrCommom.observe(this , Observer {
             when(it?.status) {
                 Status.LOADING -> {
                  progressDialogAvl.isLoading(true)

              }
                 Status.SUCCESS ->{
                     hideLoading()
                     when(it.message){
                         "getProfile" ->{
                             val myDataModel : GetProfileApiResponse ? = ImageUtils.parseJson(it.data.toString())
                             if (myDataModel != null){
                                 if(myDataModel.userExists != null){
                                     Log.i("data", "setObserver: ${myDataModel.userExists}")
                                     binding.bean = myDataModel.userExists

                                 }
                             }
                         }
                         "editProfile" ->{
                             val myDataModel : GetProfileApiResponse ? = ImageUtils.parseJson(it.data.toString())
                             if (myDataModel != null){
                                 if(myDataModel.userExists != null){
                                    showToast("Profile updated successfully")
                                     onBackPressedDispatcher.onBackPressed()

                                 }
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

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
                R.id.iv_back ->{
                    onBackPressedDispatcher.onBackPressed()
                }
                R.id.tvUpdateBtn ->{
                    val multipartImage = imageUri?.let { convertImageToMultipart(it) }
                       val data = HashMap<String,RequestBody>()
                        data["fullName"] = getRequestBody(binding.etName.text.toString().trim())
                        viewModel.editProfile(com.tech.arch.data.api.Constants.UPDATE_PROFILE,multipartImage,data)

                }
                R.id.profileImage, R.id.ivPicture -> {
                    ImagePicker.with(this)
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .createIntent { intent ->
                            startForImageResult.launch(intent)
                        }
                }

            }
        })
    }

    private val startForImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        try {
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == RESULT_OK) {
                val fileUri = data?.data
                binding.profileImage.setImageURI(fileUri)
                imageUri = fileUri
                Log.i("dasd", ": $imageUri")
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Task canceled", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun convertImageToMultipart(imageUri: Uri): MultipartBody.Part {
        val file = FileUtil.getTempFile(this, imageUri)
        return MultipartBody.Part.createFormData(
            "profileImage",
            file!!.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )
    }

    fun getRequestBody(text: String): okhttp3.RequestBody {
        return text.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}