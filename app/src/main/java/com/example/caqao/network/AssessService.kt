package com.example.caqao.network

import com.example.caqao.models.CacaoDetectionViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://192.168.1.7:5000"

val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(
        MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CacaoApiService {
    @Multipart
    @POST("assess")
    suspend fun assess(
        @Part image: MultipartBody.Part,
        @Part("beanSize") beanSize: Int,
    ): CacaoDetection

    @Multipart
    @POST("validate_image")
    suspend fun validateImage(
        @Part image: MultipartBody.Part,
    ): ImageValidationStatus

    @POST("save_results")
    @FormUrlEncoded
    suspend fun saveDetectionResults(
        @Header("x-access-token") token: String,
        @Field("imgSrcUrl") imgSrcUrl: String,
    )

    @GET("detections")
    suspend fun getDetections(@Header("x-access-token") token: String): List<CacaoDetection>

    @POST("get_detection_with_id")
    @FormUrlEncoded
    suspend fun getDetectionWithId(
        @Header("x-access-token") token: String,
        @Field("cacaoDetectionId") cacaoDetectionId: Int,
    ): CacaoDetection

    @POST("delete")
    @FormUrlEncoded
    suspend fun deleteDetectionWithId(
        @Header("x-access-token") token: String,
        @Field("cacaoDetectionId") cacaoDetectionId: Int,
    )

    @POST("register")
    suspend fun createUser(@Body user: User): UserAccountCreationStatus
    @POST("login")
    suspend fun loginUser(@Body user: User): UserLoginStatus
}

object CacaoApi {
    val retrofitService : CacaoApiService by lazy {
        retrofit.create(CacaoApiService::class.java) }
}