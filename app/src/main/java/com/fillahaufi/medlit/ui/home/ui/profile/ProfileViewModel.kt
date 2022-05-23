package com.fillahaufi.medlit.ui.home.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fillahaufi.medlit.data.UserRepository
import com.fillahaufi.medlit.data.local.UserPreference
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository, private val pref: UserPreference) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}