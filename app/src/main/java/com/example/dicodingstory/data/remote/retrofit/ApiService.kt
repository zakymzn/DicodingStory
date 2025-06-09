package com.example.dicodingstory.data.remote.retrofit

import com.example.dicodingstory.data.remote.response.StoryBasicResponse
import com.example.dicodingstory.data.remote.response.StoryDetailResponse
import com.example.dicodingstory.data.remote.response.StoryListResponse
import com.example.dicodingstory.data.remote.response.StoryLoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun registerAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): StoryBasicResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginAccount(
        @Field("email") email: String,
        @Field("password") password: String,
    ): StoryLoginResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
    ): StoryBasicResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Boolean,
    ): StoryListResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): StoryDetailResponse
}