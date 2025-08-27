package com.tech.arch.ui.home_screens.add_listing

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
class AddListingFragmentVm @Inject constructor(private val  apiHelper: ApiHelper) : BaseViewModel() {

    val obrCommon = SingleRequestEvent<JsonObject>()

    fun getMarkerList(url : String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiGetOutWithQueryAuth(url)
                if (response.isSuccessful && response.body() != null){
                    obrCommon.postValue(Resource.success("getMarkerList",response.body()))
                }
                else{
                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody() , response.code()),null))
                }
            }catch (e : Exception){
                obrCommon.postValue(Resource.error(e.message.toString(),null))
            }
        }
    }

    fun createList(data : HashMap<String, Any>, url: String){
//        CoroutineScope(Dispatchers.IO).launch{
//            obrCommon.postValue(Resource.loading(null))
//            try {
//                val response = apiHelper.apiForRawBodyAuth(data,url)
//                if (response.isSuccessful && response.body() != null){
//                    obrCommon.postValue(Resource.success("createList",response.body()))
//                }
//                else{
//                    obrCommon.postValue(Resource.error(handleErrorResponse(response.errorBody() ,response.code()),null))
//                }
//            }catch (e : Exception){
//                obrCommon.postValue(Resource.error(e.message.toString(),null))
//            }
//        }


    }


    fun addContribution(data: HashMap<String, Any>, url: String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiForRawBodyAuth(data, url)
                if (response.isSuccessful && response.body() != null) {
                    obrCommon.postValue(Resource.success("addContribution", response.body()))
                } else {
                    obrCommon.postValue(
                        Resource.error(
                            handleErrorResponse(
                                response.errorBody(),
                                response.code()
                            ), null
                        )
                    )
                }
            } catch (e: Exception) {
                obrCommon.postValue(Resource.error(e.message.toString(), null))
            }
        }
    }


}