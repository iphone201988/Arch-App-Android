package com.tech.arch.ui.auth.forgot_password

import com.tech.arch.data.api.ApiHelper
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.Resource
import com.tech.arch.utils.event.SingleRequestEvent
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordActivityVm @Inject constructor(private val apiHelper: ApiHelper) : BaseViewModel() {

    val obrCommon = SingleRequestEvent<JsonObject>()

    fun forgotPassword(data : HashMap<String, Any>, url : String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiForRawBody(data, url)
                if(response.isSuccessful && response.body() != null){
                    obrCommon.postValue(Resource.success("forgotPassword",response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody() , response.code()),null))

                }
            }catch (e: Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }
}