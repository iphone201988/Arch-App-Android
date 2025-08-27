package com.tech.arch.ui.home_screens.profile_module

import com.tech.arch.data.api.ApiHelper
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.event.SingleRequestEvent
import com.google.gson.JsonObject
import com.tech.arch.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentVm @Inject constructor(private val apiHelper: ApiHelper) : BaseViewModel() {

    val obrCommom = SingleRequestEvent<JsonObject>()

    fun logout( url : String){
//        CoroutineScope(Dispatchers.IO).launch {
//            obrCommom.postValue(Resource.loading(null))
//            try {
//                val response =  apiHelper.apiPostWithoutBody( url)
//                if (response.isSuccessful && response.body()  != null){
//                    obrCommom.postValue(Resource.success("logout",response.body()))
//                }
//                else{
//                    obrCommom.postValue(Resource.error(handleErrorResponse(response.errorBody(),response.code()),null))
//                }
//            }catch (e : Exception){
//                obrCommom.postValue(Resource.error(e.message.toString(),null))
//            }
//        }
    }

    fun deleteAccount( url : String){
//        CoroutineScope(Dispatchers.IO).launch {
//            obrCommom.postValue(Resource.loading(null))
//            try {
//                val response =  apiHelper.deleteAccount( url)
//                if (response.isSuccessful && response.body()  != null){
//                    obrCommom.postValue(Resource.success("deleteAccount",response.body()))
//                }
//                else{
//                    obrCommom.postValue(Resource.error(handleErrorResponse(response.errorBody(),response.code()),null))
//                }
//            }catch (e : Exception){
//                obrCommom.postValue(Resource.error(e.message.toString(),null))
//            }
//        }
    }

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


}