package com.fillahaufi.medlit.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Medicine(
    @ColumnInfo(name = "id")
    var id: String?,

    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "photo")
    var photo: String?,

    ) : Parcelable