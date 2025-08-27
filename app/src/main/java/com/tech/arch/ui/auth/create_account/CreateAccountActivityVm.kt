package com.tech.arch.ui.auth.create_account

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
class CreateAccountActivityVm @Inject constructor(private val apiHelper: ApiHelper): BaseViewModel() {

    val obrCommon = SingleRequestEvent<JsonObject>()

    fun createAccount(data : HashMap<String,Any>, url : String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response =  apiHelper.apiForRawBody(data, url)
                if (response.isSuccessful && response.body()  != null){
                    obrCommon.postValue(Resource.success("createAccount",response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody() , response.code()),null))
                }
            }catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }



    fun  signUp(url: String, image : MultipartBody.Part ?, data : HashMap<String , RequestBody>){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiForPostMultipartWithoutAuth(url,data,image)
                if (response.isSuccessful && response.body() != null){
                    obrCommon.postValue(Resource.success("signUp",response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody(), response.code()),null))
                }
            }catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }

    }
}