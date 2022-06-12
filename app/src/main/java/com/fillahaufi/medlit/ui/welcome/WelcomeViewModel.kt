package com.fillahaufi.medlit.ui.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fillahaufi.medlit.data.UserRepository
import com.fillahaufi.medlit.data.local.User
import com.fillahaufi.medlit.data.local.UserPreference
import kotlinx.coroutines.launch

class WelcomeViewModel (private val userRepository: UserRepository, private val pref: UserPreference?): ViewModel() {

    fun logIn(email: String, password: String) = userRepository.LogIn(email, password)
    fun setUserToken(userToken: String, userName: String, userEmail: String) = userRepository.setUserToken(userToken, userName, userEmail)

    fun getUser(): LiveData<User> {
        return pref?.getUser()?.asLiveData()!!
    }

    fun login() {
        viewModelScope.launch {
            pref?.login()
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref?.logout()
        }
    }

}