package com.tech.arch.ui.home_screens.location

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
class LocationFragmentVm @Inject constructor(private val apiHelper: ApiHelper) : BaseViewModel() {

     val obrCommon  = SingleRequestEvent<JsonObject>()

    fun getList(url : String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiGetOutWithQueryAuth(url)
                if (response.isSuccessful && response.body() != null){
                    obrCommon.postValue(Resource.success("getList",response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody(), response.code()),null))
                }
            }catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }
    fun getAllList(data : HashMap<String,Any>, url : String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiGetWithQueryAuth(data,url)
                if (response.isSuccessful && response.body() != null){
                    obrCommon.postValue(Resource.success("getAllList", response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody() , response.code()),null))
                }
            }
            catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }
    fun sendPoints(data: HashMap<String,Any>, url: String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiForRawBodyAuth(data,url)
                if (response.isSuccessful && response.body() != null){
                    obrCommon.postValue(Resource.success("sendPoints", response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody() , response.code()),null))
                }
            }
            catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }

    fun getPoints(url : String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiGetOutWithQueryAuth(url)
                if (response.isSuccessful && response.body() != null){
                    obrCommon.postValue(Resource.success("getPoints", response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody() , response.code()),null))
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

    fun getProfile( url : String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response =  apiHelper.apiGetOutWithQueryAuth( url)
                if (response.isSuccessful && response.body()  != null){
                    obrCommon.postValue(Resource.success("getProfile",response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody() ,response.code()),null))
                }
            }catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }


    fun getMaps( url : String , data : HashMap<String, Any>){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response =  apiHelper.apiGetWithQueryAuth( data,url)
                if (response.isSuccessful && response.body()  != null){
                    obrCommon.postValue(Resource.success("getMaps",response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody() ,response.code()),null))
                }
            }catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }


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
}