package com.tech.arch.data.api

import com.tech.arch.data.model.User
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

//    @Header("Authorization") token: String,


    @POST
    suspend fun apiForRawBody(@Body data: HashMap<String, Any>, @Url url: String): Response<JsonObject>

    @POST
    suspend fun apiForRawBodyAuth(@Body data: HashMap<String, Any>, @Url url: String, @Header("Authorization") token: String): Response<JsonObject>

    @POST
    suspend fun apiPostWithoutBody(@Url url: String , @Header("Authorization") token: String): Response<JsonObject>


    @FormUrlEncoded
    @POST
    suspend fun apiForFormData(
        @FieldMap data: HashMap<String, Any>,
        @Url url: String
    ): Response<JsonObject>

    @FormUrlEncoded
    @POST
    suspend fun apiForFormDataWithAuth(
        @FieldMap data: HashMap<String, Any>,
        @Url url: String,
        @Header("Authorization") token: String
    ): Response<JsonObject>


    @Multipart
    @JvmSuppressWildcards
    @PUT
    suspend fun apiPutMultipart(@PartMap data: Map<String, RequestBody>, @Part image: MultipartBody.Part?, @Url url: String,   @Header("Authorization") token: String): Response<JsonObject>
    @GET
    suspend fun apiGetOutWithQuery(@Url url: String): Response<JsonObject>


    @GET
    suspend fun apiGetOutWithQueryAuth(@Url url: String, @Header("Authorization") token: String): Response<JsonObject>

    @GET
    suspend fun apiGetWithQuery(@Url url: String, @QueryMap data : HashMap<String, String>): Response<JsonObject>

    @GET
    suspend fun apiGetWithQueryAuth(@Url url: String, @QueryMap data : HashMap<String, Any>,  @Header("Authorization") token: String): Response<JsonObject>

    @Headers(Constants.HEADER_API)
    @Multipart
    @JvmSuppressWildcards
    @POST
    suspend fun apiForPostMultipart(
        @Url url: String,
        @Header("Authorization") token: String,
        @PartMap data: Map<String, RequestBody>,
        @Part parts:MutableList<MultipartBody.Part>
    ): Response<JsonObject>


    @Multipart
    @JvmSuppressWildcards
    @POST
    suspend fun apiForPostMultipartWithoutAuth(
        @Url url: String,
        @PartMap data: Map<String, RequestBody>?,
        @Part image: MultipartBody.Part?
    ): Response<JsonObject>
    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @Headers(Constants.HEADER_API)
    @DELETE
    suspend fun deleteAccount(@Url url: String, @Header("Authorization") token: String) : Response<JsonObject>


}