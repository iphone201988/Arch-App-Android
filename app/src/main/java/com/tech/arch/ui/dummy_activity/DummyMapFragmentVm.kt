package com.tech.arch.ui.dummy_activity

import com.google.gson.JsonObject
import com.tech.arch.data.api.ApiHelper
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.Resource
import com.tech.arch.utils.event.SingleRequestEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DummyMapFragmentVm @Inject constructor(private val apiHelper: ApiHelper) : BaseViewModel() {

    val obrCommon  = SingleRequestEvent<JsonObject>()

    fun liked(url : String , data : HashMap<String,Any>){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiForFormDataWithAuth(data,url)
                if (response.isSuccessful && response.body() != null){
                    obrCommon.postValue(Resource.success("liked",response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody(), response.code()),null))
                }
            }catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }

    fun disLike(url : String , data : HashMap<String,Any>){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiForFormDataWithAuth(data,url)
                if (response.isSuccessful && response.body() != null){
                    obrCommon.postValue(Resource.success("disLike",response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody(), response.code()),null))
                }
            }catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }

    fun saveBookmark(url : String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiPostWithoutBody(url)
                if (response.isSuccessful && response.body() != null){
                    obrCommon.postValue(Resource.success("saveBookmark",response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody(), response.code()),null))
                }
            }catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }

    fun logout( url : String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response =  apiHelper.apiPostWithoutBody( url)
                if (response.isSuccessful && response.body()  != null){
                    obrCommon.postValue(Resource.success("logout",response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody(),response.code()),null))
                }
            }catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }

}