package com.tech.arch.ui.auth.create_account

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.gson.Gson
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.tech.arch.data.model.SignUpApiResponse
import com.tech.arch.databinding.ActivityAccountLastStepBinding
import com.tech.arch.databinding.ActivityAccountSecondStepBinding
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.home_screens.HomeDashBoardActivity
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class AccountLastStepActivity : BaseActivity<ActivityAccountLastStepBinding>() {
    private val viewModel : CreateAccountActivityVm by viewModels()


    override fun getLayoutResource(): Int {
        return R.layout.activity_account_last_step
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    companion object {
        var lat = 0.0
        var long = 0.0
    }

    override fun onCreateView() {
        ImageUtils.getStatusBarColor(this)
        initView()
        initOnClick()
        setObserver()

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
                            "signUp" ->{
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

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
                R.id.tvContinue ->{


                    val accountType = Constants.accountType
                    when(accountType){
                        "Individual User" ->{
                            val multipartImage = RegistrationDataHolder.profileImage?.let { convertImageToMultipart(it) }
                            if (multipartImage != null){

//                                val gson = Gson()
//                                val certJson = gson.toJson(RegistrationDataHolder.certifications)

                                val data = HashMap<String, RequestBody>()
                                data["email"] = RegistrationDataHolder.email.toString().toRequestBody()
                                data["password"] = RegistrationDataHolder.password.toString().toRequestBody()
                                data["fullName"] = RegistrationDataHolder.fullName.toString().toRequestBody()
                                data["accountType"] = RegistrationDataHolder.accountType.toString().toRequestBody()
                                data["phone"] = RegistrationDataHolder.phone.toString().toRequestBody()
                                data["countryCode"] = "+1".toRequestBody()
                                data["deviceType"] = "1".toRequestBody()
                                data["lat"] = lat.toString().toRequestBody()
                                data["lng"] = long.toString().toRequestBody()
                                data["timeZone"]  = Constants.timeZone.toRequestBody()
                                data["deviceToken"] = Constants.deviceToken.toRequestBody()
                                data["certifications"] = RegistrationDataHolder.certifications.toString().toRequestBody()
                                data["diveHistory"] = RegistrationDataHolder.diveHistory.toString().toRequestBody()
                                data["interestedArea"] = RegistrationDataHolder.interestedArea.toString().toRequestBody()
                                data["mapInteraction"] = RegistrationDataHolder.mapInteraction.toString().toRequestBody()
                                data["termsAccepted"] = RegistrationDataHolder.termsAccepted.toString().toRequestBody()

                                viewModel.signUp(Constants.SIGNUP,multipartImage,data)

                                // Log all request body data
                                for ((key, value) in data) {
                                    try {
                                        val buffer = okio.Buffer()
                                        value.writeTo(buffer)
                                        Log.d("SignUpData", "$key : ${buffer.readUtf8()}")
                                    } catch (e: Exception) {
                                        Log.e("SignUpData", "Error reading $key", e)
                                    }
                                }
                            }else{
                                showToast("Please select image")
                            }

                        }
                        "Group" ->{
                            val multipartImage = RegistrationDataHolder.profileImage?.let { convertImageToMultipart(it) }
                            if (multipartImage != null){

                                val data = HashMap<String, RequestBody>()
                                data["email"] = RegistrationDataHolder.email.toString().toRequestBody()
                                data["password"] = RegistrationDataHolder.password.toString().toRequestBody()
                                data["fullName"] = RegistrationDataHolder.fullName.toString().toRequestBody()
                                data["accountType"] = RegistrationDataHolder.accountType.toString().toRequestBody()
                                data["phone"] = RegistrationDataHolder.phone.toString().toRequestBody()
                                data["countryCode"] = "+1".toRequestBody()
                                data["deviceType"] = "1".toRequestBody()
                                data["lat"] = lat.toString().toRequestBody()
                                data["lng"] = long.toString().toRequestBody()
                                data["timeZone"]  = Constants.timeZone.toRequestBody()
                                data["deviceToken"] = Constants.deviceToken.toRequestBody()
                                data["groupName"] = RegistrationDataHolder.groupName.toString().toRequestBody()
                                data["groupContactEmail"] = RegistrationDataHolder.groupContactEmail.toString().toRequestBody()
                                data["groupType"] = RegistrationDataHolder.groupType.toString().toRequestBody()
                                data["numberOfMembers"] = RegistrationDataHolder.numberOfMembers.toString().toRequestBody()
                                data["groupMembersRoles"]
                                data["groupHostedEvents"] = RegistrationDataHolder.groupHostedEvent.toString().toRequestBody()
                                data["groupWebsite"] = RegistrationDataHolder.groupWebsite.toString().toRequestBody()
                                data["groupSocialMedia"]
                                data["openToNewMembers"] = RegistrationDataHolder.openToNewMembers.toString().toRequestBody()
                                data["termsAccepted"] = RegistrationDataHolder.termsAccepted.toString().toRequestBody()

                                // Log all request body data
                                for ((key, value) in data) {
                                    try {
                                        val buffer = okio.Buffer()
                                        value.writeTo(buffer)
                                        Log.d("SignUpData", "$key : ${buffer.readUtf8()}")
                                    } catch (e: Exception) {
                                        Log.e("SignUpData", "Error reading $key", e)
                                    }
                                }
                                viewModel.signUp(Constants.SIGNUP,multipartImage,data)
                            }else{
                                showToast("Please select image")
                            }
                        }
                        "Business" ->{
                            val multipartImage = RegistrationDataHolder.profileImage?.let { convertImageToMultipart(it) }
                            if (multipartImage != null){

//                                val gson = Gson()
//                                val certJson = gson.toJson(RegistrationDataHolder.certifications)

                                val data = HashMap<String, RequestBody>()
                                data["email"] = RegistrationDataHolder.email.toString().toRequestBody()
                                data["password"] = RegistrationDataHolder.password.toString().toRequestBody()
                                data["fullName"] = RegistrationDataHolder.fullName.toString().toRequestBody()
                                data["accountType"] = RegistrationDataHolder.accountType.toString().toRequestBody()
                                data["phone"] = RegistrationDataHolder.phone.toString().toRequestBody()
                                data["countryCode"] = "+1".toRequestBody()
                                data["deviceType"] = "1".toRequestBody()
                                data["lat"] = lat.toString().toRequestBody()
                                data["lng"] = long.toString().toRequestBody()
                                data["timeZone"]  = Constants.timeZone.toRequestBody()
                                data["deviceToken"] = Constants.deviceToken.toRequestBody()
                                data["businessName"] = RegistrationDataHolder.businessName.toString().toRequestBody()
                                data["businessType"] = RegistrationDataHolder.businessType.toString().toRequestBody()
                                data["businessPhone"] = RegistrationDataHolder.businessPhone.toString().toRequestBody()
                                data["publicEmail"] = RegistrationDataHolder.publicEmail.toString().toRequestBody()
                                data["website"] = RegistrationDataHolder.website.toString().toRequestBody()
                                data["socialMedia"] = RegistrationDataHolder.businessSocialMedia.toString().toRequestBody()
                                data["servicesOffered"] = RegistrationDataHolder.servicesOffered.toString().toRequestBody()
                                data["address"] = RegistrationDataHolder.address.toString().toRequestBody()
                                data["hoursOfOperation"] = RegistrationDataHolder.hoursOfOperation.toString().toRequestBody()
                                data["hostedEvents"] = RegistrationDataHolder.hostedEvents.toString().toRequestBody()
                                data["businessRole"] = RegistrationDataHolder.businessRole.toString().toRequestBody()
                                data["termsAccepted"] = RegistrationDataHolder.termsAccepted.toString().toRequestBody()

                                viewModel.signUp(Constants.SIGNUP,multipartImage,data)

                                // Log all request body data
                                for ((key, value) in data) {
                                    try {
                                        val buffer = okio.Buffer()
                                        value.writeTo(buffer)
                                        Log.d("SignUpData", "$key : ${buffer.readUtf8()}")
                                    } catch (e: Exception) {
                                        Log.e("SignUpData", "Error reading $key", e)
                                    }
                                }
                            }else{
                                showToast("Please select image")
                            }
                        }
                    }
//                    val intent = Intent(this, HomeDashBoardActivity::class.java)
//                    startActivity(intent)
                }
            }
        })
    }

    private fun initView() {
        var accountType = Constants.accountType
        binding.bean = RegistrationDataHolder
        when(accountType){
            "Individual User" ->{
                binding.consIndividual.visibility = View.VISIBLE
                binding.consBusiness.visibility = View.GONE
                binding.consGroup.visibility = View.GONE
            }
            "Group" ->{
                binding.consIndividual.visibility = View.GONE
                binding.consBusiness.visibility = View.GONE
                binding.consGroup.visibility = View.VISIBLE
            }
            "Business" ->{
                binding.consIndividual.visibility = View.GONE
                binding.consBusiness.visibility = View.VISIBLE
                binding.consGroup.visibility = View.GONE
            }
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

}