package com.fillahaufi.medlit.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Medicine(
    @ColumnInfo(name = "id")
    @PrimaryKey
    var id: Int,

    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "photo")
    var photo: String?,

    @ColumnInfo(name = "isBookmarked")
    var isBookmarked: Boolean?,

    ) : Parcelable