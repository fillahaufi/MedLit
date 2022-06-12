package com.fillahaufi.medlit.ui.camera

import androidx.lifecycle.ViewModel
import com.fillahaufi.medlit.data.UserRepository
import com.fillahaufi.medlit.data.local.UserPreference

class ScanResultViewModel(private val userRepository: UserRepository, private val pref: UserPreference?): ViewModel() {

    fun getSearchMedicines(medicine: String) = userRepository.getSearchMedicine(medicine)

}