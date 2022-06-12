package com.fillahaufi.medlit.ui.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fillahaufi.medlit.data.UserRepository
import com.fillahaufi.medlit.data.local.UserPreference

class HomeViewModel(private val userRepository: UserRepository, private val pref: UserPreference?) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getAllMedicines(authHeader: String) = userRepository.getAllMedicine(authHeader)

}