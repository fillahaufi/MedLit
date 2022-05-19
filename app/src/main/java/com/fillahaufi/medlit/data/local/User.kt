package com.fillahaufi.medlit.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class User(
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "token")
    var token: String,

    @ColumnInfo(name = "isLoggedin")
    var isLoggedin: Boolean,

) : Parcelable