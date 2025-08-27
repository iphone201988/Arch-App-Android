package com.tech.arch.ui.home_screens.edit_your_profile

import com.tech.arch.data.api.ApiHelper
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.Resource
import com.tech.arch.utils.event.SingleRequestEvent
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class EditYourProfileVm @Inject constructor(private val apiHelper: ApiHelper) : BaseViewModel() {

    val obrCommom = SingleRequestEvent<JsonObject>()

        fun getProfile( url : String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommom.postValue(Resource.loading(null))
            try {
                val response =  apiHelper.apiGetOutWithQueryAuth( url)
                if (response.isSuccessful && response.body()  != null){
                    obrCommom.postValue(Resource.success("getProfile",response.body()))
                }
                else{
                    obrCommom.postValue(Resource.error(handleErrorResponse(response.errorBody() ,response.code()),null))
                }
            }catch (e : Exception){
                obrCommom.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }

    fun  editProfile(url: String, image : MultipartBody.Part ?, data : HashMap<String , RequestBody>){
       CoroutineScope(Dispatchers.IO).launch {
           obrCommom.postValue(Resource.loading(null))
           try {
               val response = apiHelper.apiPutMultipart(url,data,image)
               if (response.isSuccessful && response.body() != null){
                   obrCommom.postValue(Resource.success("editProfile",response.body()))
               }
               else{
                   obrCommom.postValue(Resource.error(handleErrorResponse(response.errorBody(), response.code()),null))
               }
           }catch (e : Exception){
               obrCommom.postValue(Resource.error(e.message.toString(),null))
           }
       }

    }
}