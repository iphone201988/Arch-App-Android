package com.tech.arch.data.api

import com.tech.arch.data.model.User
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface ApiHelper {
    suspend fun getUsers(): Response<List<User>>

    suspend fun apiForRawBody(request:HashMap<String, Any>,url: String, ): Response<JsonObject>
    suspend fun apiForFormData(data: HashMap<String, Any>,url: String): Response<JsonObject>

    suspend fun apiForFormDataWithAuth(data: HashMap<String, Any>, url: String): Response<JsonObject>
    suspend fun apiGetOutWithQuery(url:String): Response<JsonObject>
//    suspend fun getDropDown(): Response<JsonObject>
    suspend fun apiGetWithQuery(data: HashMap<String, String>,url: String): Response<JsonObject>
//    suspend fun getCity(id: String): Response<JsonObject>
    suspend fun apiForPostMultipart(url: String,map: HashMap<String, RequestBody>,
                                 part: MutableList<MultipartBody.Part>): Response<JsonObject>

    suspend fun apiGetOutWithQueryAuth(url: String): Response<JsonObject>

    suspend fun apiPostWithoutBody(url: String) : Response<JsonObject>

    suspend fun apiPutMultipart(url: String, data: HashMap<String, RequestBody>, part: MultipartBody.Part?) : Response<JsonObject>


    suspend fun apiForRawBodyAuth(data: HashMap<String, Any>, url: String)  : Response<JsonObject>

    suspend fun apiGetWithQueryAuth(data: HashMap<String, Any>, url: String) : Response<JsonObject>

    suspend fun deleteAccount(url: String) : Response<JsonObject>

    suspend fun apiForPostMultipartWithoutAuth(url: String,map: HashMap<String, RequestBody>, part: MultipartBody.Part?): Response<JsonObject>
}