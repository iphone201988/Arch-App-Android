package com.tech.arch.ui.home_screens.bookmark_module

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
class BookmarkActivityVm @Inject constructor(private val apiHelper: ApiHelper) : BaseViewModel() {

    val obrCommon  = SingleRequestEvent<JsonObject>()

    fun getBookMarks(url : String){
        CoroutineScope(Dispatchers.IO).launch {
            obrCommon.postValue(Resource.loading(null))
            try {
                val response = apiHelper.apiGetOutWithQueryAuth(url)
                if (response.isSuccessful && response.body() != null){
                    obrCommon.postValue(Resource.success("getBookMarks",response.body()))
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