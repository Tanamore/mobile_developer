package com.example.mytanamore.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class EnsiklopediaResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class EnsiklopediaDetailResponse(

	@field:SerializedName("data")
	val data: DataItem? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class SearchEnsiklopediaResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class DataItem(

	@field:SerializedName("plant_id")
	val plantId: String? = null,

	@field:SerializedName("origin_place")
	val originPlace: String? = null,

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("watering_time")
	val wateringTime: Double? = null,

	@field:SerializedName("scientific_name")
	val scientificName: String? = null,

	@field:SerializedName("climate")
	val climate: String? = null,

	@field:SerializedName("watering_frequency")
	val wateringFrequency: String? = null,

	@field:SerializedName("watering_interval")
	val wateringInterval: String? = null,

	@field:SerializedName("fertilizer")
	val fertilizer: String? = null,

	@field:SerializedName("common_disease")
	val commonDisease: String? = null,

	@field:SerializedName("harvest_time_days")
	val harvestTimeDays: Double? = null,

	@field:SerializedName("uses")
	val uses: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("harvest_time")
	val harvestTime: String? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null
) : Parcelable
