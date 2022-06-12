package com.fillahaufi.medlit.ui.others

import androidx.lifecycle.ViewModel
import com.fillahaufi.medlit.data.UserRepository
import com.fillahaufi.medlit.data.local.Medicine
import com.fillahaufi.medlit.data.local.UserPreference

class BookmarkViewModel(private val userRepository: UserRepository, private val pref: UserPreference?): ViewModel() {

    fun getBookmarkedUsers() = userRepository.getBookmarkedUser()

    fun saveUsers(medicin: Medicine) {
        userRepository.setBookmarkedUsers(medicin, true)
    }

    fun deleteUsers(medicine: Medicine) {
        userRepository.setBookmarkedUsers(medicine, false)
    }
}