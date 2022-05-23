package com.fillahaufi.medlit.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegisterResponse(

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable
