package com.tech.arch.data.api

import com.tech.arch.data.local.SharedPrefManager
import com.tech.arch.data.model.User
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService,  private val sharedPreferences: SharedPrefManager) : ApiHelper {

    override suspend fun getUsers(): Response<List<User>> = apiService.getUsers()
    override suspend fun apiForRawBody(request: HashMap<String, Any>,url:String): Response<JsonObject> {
        return apiService.apiForRawBody(request,url)
    }override suspend fun apiForFormData(data: HashMap<String, Any>,url: String): Response<JsonObject> {
        return apiService.apiForFormData(data,url)
    }

    override suspend fun apiForFormDataWithAuth(
        data: HashMap<String, Any>,
        url: String
    ): Response<JsonObject> {
        return apiService.apiForFormDataWithAuth(data,url,getTokenFromSPref())
    }

    override suspend fun apiGetOutWithQuery(url:String): Response<JsonObject> {
        return apiService.apiGetOutWithQuery(url)
    }override suspend fun apiGetWithQuery( data: HashMap<String, String>,url: String): Response<JsonObject> {
        return apiService.apiGetWithQuery(url,data)
    }
    override suspend fun apiForPostMultipart(url: String,map: HashMap<String, RequestBody>,
                                          part: MutableList<MultipartBody.Part>): Response<JsonObject> {
        return apiService.apiForPostMultipart(url,getTokenFromSPref(), map, part)
    }

    override suspend fun apiGetOutWithQueryAuth(url: String): Response<JsonObject> {
        return apiService.apiGetOutWithQueryAuth(url, getTokenFromSPref())
    }

    override suspend fun apiPostWithoutBody(url: String): Response<JsonObject> {
        return apiService.apiPostWithoutBody(url, getTokenFromSPref())
    }

    override suspend fun apiPutMultipart(
        url: String,
        data: HashMap<String, RequestBody>,
        part: MultipartBody.Part?
    ): Response<JsonObject> {
        return apiService.apiPutMultipart(data,part,url,getTokenFromSPref())
    }

    override suspend fun apiForRawBodyAuth(
        data: HashMap<String, Any>,
        url: String
    ): Response<JsonObject> {
        return apiService.apiForRawBodyAuth(data,url,getTokenFromSPref())
    }

    override suspend fun apiGetWithQueryAuth(
        data: HashMap<String, Any>,
        url: String
    ): Response<JsonObject> {
        return apiService.apiGetWithQueryAuth(url,data,getTokenFromSPref())
    }

    override suspend fun deleteAccount(url: String): Response<JsonObject> {
        return apiService.deleteAccount(url,getTokenFromSPref())
    }

    override suspend fun apiForPostMultipartWithoutAuth(
        url: String,
        map: HashMap<String, RequestBody>,
        part: MultipartBody.Part?
    ): Response<JsonObject> {
        return apiService.apiForPostMultipartWithoutAuth(url,map,part)
    }

    private fun getTokenFromSPref(): String {

        return "Bearer ${
            sharedPreferences.getToken()
        }"
    }
}