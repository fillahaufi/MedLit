package com.fillahaufi.medlit.di

import android.content.Context
import com.fillahaufi.medlit.data.UserRepository
import com.fillahaufi.medlit.data.remote.retrofit.ApiConfig
import com.fillahaufi.medlit.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
//        val database = UserRoomDatabase.getDatabase(context)
//        val dao = database.userDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, appExecutors)
    }
}