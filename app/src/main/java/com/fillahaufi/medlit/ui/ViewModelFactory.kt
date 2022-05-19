package com.fillahaufi.medlit.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fillahaufi.medlit.data.local.UserPreference
import com.fillahaufi.medlit.di.Injection
import com.fillahaufi.medlit.ui.auth.AuthViewModel

class ViewModelFactory private constructor(private val pref: UserPreference): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
//        private lateinit var pref: UserPreference
        private var instance: ViewModelFactory? = null
//        fun getInstance(context: Context, pref: UserPreference): ViewModelFactory =
//            instance ?: synchronized(this) {
//                instance ?: ViewModelFactory(Injection.provideRepository(context), pref)
//            }.also { instance = it }
    }

}