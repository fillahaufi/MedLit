package com.fillahaufi.medlit.data.remote.retrofit.body

class SignupBodyInformation {
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    fun setName(name: String) {
        this.name = name
    }
    fun setEmail(email: String) {
        this.email = email
    }
    fun setPassword(password: String) {
        this.password = password
    }

}