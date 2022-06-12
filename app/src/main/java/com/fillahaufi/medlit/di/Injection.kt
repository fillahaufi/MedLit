package com.fillahaufi.medlit.di

import android.content.Context
import com.fillahaufi.medlit.data.UserRepository
import com.fillahaufi.medlit.data.local.room.MedRoomDatabase
import com.fillahaufi.medlit.data.remote.retrofit.ApiConfig
import com.fillahaufi.medlit.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = MedRoomDatabase.getDatabase(context)
        val dao = database.medDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}