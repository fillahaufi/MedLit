package com.fillahaufi.medlit.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fillahaufi.medlit.data.UserRepository
import com.fillahaufi.medlit.data.local.UserPreference
import com.fillahaufi.medlit.di.Injection
import com.fillahaufi.medlit.ui.auth.AuthViewModel
import com.fillahaufi.medlit.ui.camera.ScanResultViewModel
import com.fillahaufi.medlit.ui.home.ui.home.HomeViewModel
import com.fillahaufi.medlit.ui.home.ui.profile.ProfileViewModel
import com.fillahaufi.medlit.ui.others.BookmarkViewModel
import com.fillahaufi.medlit.ui.others.SearchResultViewModel
import com.fillahaufi.medlit.ui.welcome.WelcomeViewModel

class ViewModelFactory private constructor(private val userRepository: UserRepository, private val pref: UserPreference?): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
            return WelcomeViewModel(userRepository, pref) as T
        }
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userRepository, pref) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userRepository, pref) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(userRepository, pref) as T
        }
        if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
            return BookmarkViewModel(userRepository, pref) as T
        }
        if (modelClass.isAssignableFrom(SearchResultViewModel::class.java)) {
            return SearchResultViewModel(userRepository, pref) as T
        }
        if (modelClass.isAssignableFrom(ScanResultViewModel::class.java)) {
            return ScanResultViewModel(userRepository, pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
//        private lateinit var pref: UserPreference
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, pref: UserPreference?): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), pref)
            }.also { instance = it }
    }

}