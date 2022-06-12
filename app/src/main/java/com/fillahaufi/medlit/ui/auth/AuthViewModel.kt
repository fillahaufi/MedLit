package com.fillahaufi.medlit.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fillahaufi.medlit.data.UserRepository
import com.fillahaufi.medlit.data.local.User
import com.fillahaufi.medlit.data.local.UserPreference
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository, private val pref: UserPreference?): ViewModel() {

    fun signUp(name: String, email: String, password: String, rePassword: String) = userRepository.SignUp(name, email, password, rePassword)

    fun logIn(email: String, password: String) = userRepository.LogIn(email, password)

    fun saveUser(user: User) {
        viewModelScope.launch {
            pref?.saveUser(user)
        }
    }

    fun getUser(): LiveData<User> {
        return pref?.getUser()?.asLiveData()!!
    }

    fun login() {
        viewModelScope.launch {
            pref?.login()
        }
    }

}