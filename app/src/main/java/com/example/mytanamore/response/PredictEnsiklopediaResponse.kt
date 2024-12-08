package com.example.mytanamore.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PredictEnsiklopediaResponse(

	@field:SerializedName("dataPredict")
	val data: DataPredict? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class DataPredict(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("confidence")
	val confidence: String? = null,

	@field:SerializedName("plantInfo")
	val plantInfo: PlantInfo? = null
) : Parcelable

@Parcelize
data class PlantInfo(

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
	val harvestTimeDays: Int? = null,

	@field:SerializedName("uses")
	val uses: String? = null,

	@field:SerializedName("harvest_time")
	val harvestTime: String? = null,

	@field:SerializedName("plant_name")
	val plantName: String? = null
) : Parcelable
