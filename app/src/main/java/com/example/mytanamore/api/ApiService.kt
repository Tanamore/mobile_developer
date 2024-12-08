package com.example.mytanamore.api

import com.example.mytanamore.response.DeteksiPenyakitResponse
import com.example.mytanamore.response.EnsiklopediaDetailResponse
import com.example.mytanamore.response.EnsiklopediaResponse
import com.example.mytanamore.response.PredictEnsiklopediaResponse
import com.example.mytanamore.response.SearchEnsiklopediaResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @Multipart
    @POST("diseases/predict")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<DeteksiPenyakitResponse>

    @GET("encyclopedias")
    suspend fun getEnsiklopedia(
    ) : Response<EnsiklopediaResponse>

    @GET("encyclopedias/{plant_id}")
    suspend fun getEnsiklopediaDetail(
        @Path("plant_id") plantId: String
    ) : Response<EnsiklopediaDetailResponse>

    @GET("encyclopedias/search")
    suspend fun searchEnsiklopedia(
        @Query("name") name: String
    ): Response<SearchEnsiklopediaResponse>

    @Multipart
    @POST("encyclopedias/predict")
    suspend fun uploadImageEnsiklopedia(
        @Part image: MultipartBody.Part
    ): Response<PredictEnsiklopediaResponse>

}