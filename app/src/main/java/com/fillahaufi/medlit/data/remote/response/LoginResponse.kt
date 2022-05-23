package com.fillahaufi.medlit.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class LoginResult(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("token")
	val token: String? = null
) : Parcelable
