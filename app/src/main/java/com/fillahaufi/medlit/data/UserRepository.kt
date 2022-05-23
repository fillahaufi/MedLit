package com.fillahaufi.medlit.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fillahaufi.medlit.data.remote.response.LoginResponse
import com.fillahaufi.medlit.data.remote.response.RegisterResponse
import com.fillahaufi.medlit.data.remote.retrofit.ApiService
import com.fillahaufi.medlit.data.remote.retrofit.body.LoginBodyInformation
import com.fillahaufi.medlit.data.remote.retrofit.body.SignupBodyInformation
import com.fillahaufi.medlit.utils.AppExecutors
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val appExecutors: AppExecutors
){
    private var userToken: String = "USER_TOKEN"

    private val signupResult = MediatorLiveData<Result<RegisterResponse>>()
    private val signupBodyInformation: SignupBodyInformation = SignupBodyInformation()

    private val loginResult = MediatorLiveData<Result<LoginResponse>>()
    private val loginBodyInformation: LoginBodyInformation = LoginBodyInformation()

    fun SignUp(name: String, email: String, password: String): MediatorLiveData<Result<RegisterResponse>> {
        signupResult.value = Result.Loading
        signupBodyInformation.setName(name)
        signupBodyInformation.setEmail(email)
        signupBodyInformation.setPassword(password)
        val client = apiService.signup(signupBodyInformation)
        val callbackResponse = MutableLiveData<RegisterResponse>()
        Log.d("signupBody", signupBodyInformation.toString())
        client.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    appExecutors.diskIO.execute {
                        callbackResponse.postValue(responseBody!!)
                    }
                }
                else {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    signupResult.value = Result.Error(jObjError.getString("message"))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                signupResult.value = Result.Error(t.message.toString())
            }
        })

        val localData = callbackResponse
        signupResult.addSource(localData) {
            signupResult.value = Result.Success(it)
        }
        return signupResult
    }

    fun LogIn(email: String, password: String): MediatorLiveData<Result<LoginResponse>> {
        loginResult.value = Result.Loading
        loginBodyInformation.setEmail(email)
        loginBodyInformation.setPassword(password)
        val client = apiService.login(loginBodyInformation)
        val callbackResponse = MutableLiveData<LoginResponse>()
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val loginResult = responseBody?.loginResult
                    appExecutors.diskIO.execute {
                        userToken = loginResult?.token.toString()

                        callbackResponse.postValue(responseBody!!)
                    }
                }
                else {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    loginResult.value = Result.Error(jObjError.getString("message"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResult.value = Result.Error(t.message.toString())
            }
        })

        val localData = callbackResponse
        loginResult.addSource(localData) {
            loginResult.value = Result.Success(it)
        }
        return loginResult
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
//            userDao: UserDao,
//            userDetailDao: UserDetailDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, appExecutors)
            }.also { instance = it }
    }
}