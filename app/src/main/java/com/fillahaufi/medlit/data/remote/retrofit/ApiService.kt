package com.fillahaufi.medlit.data.remote.retrofit

import com.fillahaufi.medlit.data.remote.response.AllMedicineResponse
import com.fillahaufi.medlit.data.remote.response.LoginResponse
import com.fillahaufi.medlit.data.remote.response.RegisterResponse
import com.fillahaufi.medlit.data.remote.retrofit.body.LoginBodyInformation
import com.fillahaufi.medlit.data.remote.retrofit.body.SignupBodyInformation
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("register")
    fun signup(
        @Body signupBodyInformation: SignupBodyInformation
    ): Call<RegisterResponse>

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("login")
    fun login(
        @Body loginBodyInformation: LoginBodyInformation
    ): Call<LoginResponse>

    @GET("medicine/get/all")
    fun getAllStories(
        @Header("Authorization") authHeader: String,
    ): Call<AllMedicineResponse>

    @GET("medicine/search?")
    fun searchMedicine(
        @Query("generic_name") name: String
    ): Call<AllMedicineResponse>

//
//    @GET("stories")
//    suspend fun getStories(
//        @Header("Authorization") authHeader: String,
//        @Query("page") page: Int,
//        @Query("size") size: Int,
//    ): AllStoryResponse

}