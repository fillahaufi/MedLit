package com.fillahaufi.medlit.data.remote.retrofit.body

class LoginBodyInformation {
    private lateinit var email: String
    private lateinit var password: String

    fun setEmail(email: String) {
        this.email = email
    }
    fun setPassword(password: String) {
        this.password = password
    }
}