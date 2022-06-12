package com.fillahaufi.medlit.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllMedicineResponse(

	@field:SerializedName("medicineList")
	val medicineList: List<MedicineListItem?>? = null,

	@field:SerializedName("error")
	val error: String? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class MedicineListItem(

	@field:SerializedName("side_effects")
	val sideEffects: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("dosage")
	val dosage: String? = null,


	@field:SerializedName("purpose")
	val purpose: String? = null,

	@field:SerializedName("generic_name")
	val genericName: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,


	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("photo_url")
	val photoUrl: String? = null,

	@field:SerializedName("contraindication")
	val contraindication: String? = null
) : Parcelable
