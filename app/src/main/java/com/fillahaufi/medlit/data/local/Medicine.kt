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

    @ColumnInfo(name = "purpose")
    var purpose: String?,

    @ColumnInfo(name = "side_effetcs")
    var side_effetcs: String?,

    @ColumnInfo(name = "contraindication")
    var contraindication: String?,

    @ColumnInfo(name = "dosage")
    var dosage: String?,

    @ColumnInfo(name = "ingredients")
    var ingredients: String?,

    @ColumnInfo(name = "created_at")
    var created_at: String?,

    @ColumnInfo(name = "updated_at")
    var updated_at: String?,

    @ColumnInfo(name = "isBookmarked")
    var isBookmarked: Boolean?,

    ) : Parcelable