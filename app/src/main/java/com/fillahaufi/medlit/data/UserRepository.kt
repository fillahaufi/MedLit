package com.fillahaufi.medlit.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.fillahaufi.medlit.data.local.Medicine
import com.fillahaufi.medlit.data.local.room.MedDao
import com.fillahaufi.medlit.data.remote.response.AllMedicineResponse
import com.fillahaufi.medlit.data.remote.response.LoginResponse
import com.fillahaufi.medlit.data.remote.response.RegisterResponse
import com.fillahaufi.medlit.data.remote.retrofit.ApiService
import com.fillahaufi.medlit.data.remote.retrofit.body.LoginBodyInformation
import com.fillahaufi.medlit.data.remote.retrofit.body.SignupBodyInformation
import com.fillahaufi.medlit.utils.AppExecutors
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val medDao: MedDao,
    private val appExecutors: AppExecutors
){
    private var userToken: String = "USER_TOKEN"
    private var userName: String = "USER_NAME"
    private var userEmail: String = "USER_EMAIL"

    private val signupResult = MediatorLiveData<Result<RegisterResponse>>()
    private val signupBodyInformation: SignupBodyInformation = SignupBodyInformation()

    private val loginResult = MediatorLiveData<Result<LoginResponse>>()
    private val loginBodyInformation: LoginBodyInformation = LoginBodyInformation()

    private val allMedicine = MediatorLiveData<Result<List<Medicine>>>()

    private val searchResult = MediatorLiveData<Result<List<Medicine>>>()

    fun setUserToken(token: String, name: String, email: String) {
        userToken = token
        userName = name
        userEmail = email
    }

    fun SignUp(name: String, email: String, password: String, rePassword: String): MediatorLiveData<Result<RegisterResponse>> {
        signupResult.value = Result.Loading
        signupBodyInformation.setName(name)
        signupBodyInformation.setEmail(email)
        signupBodyInformation.setPassword(password)
        signupBodyInformation.setRePassword(rePassword)
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

    fun getAllMedicine(authHeader: String): LiveData<Result<List<Medicine>>> {
        val client = apiService.getAllStories(authHeader)
        val callbackResponse = MutableLiveData<ArrayList<Medicine>>()
        client.enqueue(object: Callback<AllMedicineResponse> {
            override fun onResponse(
                call: Call<AllMedicineResponse>,
                response: Response<AllMedicineResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val medList = responseBody?.medicineList
                    val arrMedList = ArrayList<Medicine>()
                    appExecutors.diskIO.execute {
                        medList?.forEach { medicine ->
                            val medicines = Medicine(
                                medicine?.id!!,
                                medicine.genericName,
                                medicine.photoUrl,
                                medicine.purpose,
                                medicine.sideEffects,
                                medicine.contraindication,
                                medicine.dosage,
                                medicine.ingredients,
                                medicine.createdAt,
                                medicine.updatedAt,
                                false
                            )
                            arrMedList.add(medicines)
                        }
                        callbackResponse.postValue(arrMedList)
                    }
                }
                else {
                    allMedicine.value = Result.Error(response.body()?.message.toString())
                }
            }

            override fun onFailure(call: Call<AllMedicineResponse>, t: Throwable) {
                allMedicine.value = Result.Error(t.message.toString())
            }

        })

        allMedicine.addSource(callbackResponse) {
            allMedicine.value = Result.Success(it)
        }

        return allMedicine
    }

//    fun getSearchMedicine(query: String)

    fun getSearchMedicine(query: String): LiveData<Result<List<Medicine>>> {
        searchResult.value = Result.Loading
        val client = apiService.searchMedicine(query)
        val callbackResponse = MutableLiveData<ArrayList<Medicine>>()
        Log.d("prosesss", client.toString())
        client.enqueue(object: Callback<AllMedicineResponse> {
            override fun onResponse(call: Call<AllMedicineResponse>, response: Response<AllMedicineResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
//                    Log.d("userss", medicines.toString())
                    val medList = responseBody?.medicineList
                    val arrMedList = ArrayList<Medicine>()
                    appExecutors.diskIO.execute {
                        medList?.forEach { medicine ->
//                            val isBookmarked = userDao.isUsersBookmarked(user.login)
                            val medicines = Medicine(
                                medicine?.id!!,
                                medicine.genericName,
                                medicine.photoUrl,
                                medicine.purpose,
                                medicine.sideEffects,
                                medicine.contraindication,
                                medicine.dosage,
                                medicine.ingredients,
                                medicine.createdAt,
                                medicine.updatedAt,
                                false
                            )
                            arrMedList.add(medicines)
                        }
                        callbackResponse.postValue(arrMedList)
                        Log.d("userList", medList.toString())
//                        userDao.deleteAllUnbookmarked()
//                        userDao.insert(medList)
                    }
                }
            }
            override fun onFailure(call: Call<AllMedicineResponse>, t: Throwable) {
                searchResult.value = Result.Error(t.message.toString())
            }
        })
//        val localData = userDao.getSearchedUsers(query)
//        Log.d("searchedUsersss", userDao.getSearchedUsers(query).value.toString())
        searchResult.addSource(callbackResponse) { newData: List<Medicine> ->
            searchResult.value = Result.Success(newData)
        }
        return searchResult
    }

    fun getBookmarkedUser(): LiveData<List<Medicine>> {
        return medDao.getBookmarkedUsers()
    }

    fun setBookmarkedUsers(medicine: Medicine, bookmarkState: Boolean) {
        appExecutors.diskIO.execute {
            medicine.isBookmarked = bookmarkState
            medDao.update(medicine)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            medDao: MedDao,
//            userDetailDao: UserDetailDao,
            appExecutors: AppExecutors
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, medDao, appExecutors)
            }.also { instance = it }
    }
}