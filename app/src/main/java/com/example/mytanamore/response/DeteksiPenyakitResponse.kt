package com.example.mytanamore.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DeteksiPenyakitResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class DiseaseInfo(

	@field:SerializedName("symptoms")
	val symptoms: String? = null,

	@field:SerializedName("treatment")
	val treatment: String? = null,

	@field:SerializedName("prevention_tips")
	val preventionTips: String? = null,

	@field:SerializedName("disease_id")
	val diseaseId: String? = null,

	@field:SerializedName("causes")
	val causes: String? = null,

	@field:SerializedName("disease_name")
	val diseaseName: String? = null
): Parcelable

@Parcelize
data class Data(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("confidence")
	val confidence: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("diseaseInfo")
	val diseaseInfo: DiseaseInfo? = null
) : Parcelable
