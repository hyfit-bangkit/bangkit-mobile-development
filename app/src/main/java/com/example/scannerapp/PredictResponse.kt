package com.example.scannerapp

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.Call

data class PredictResponse(
    val confidence_score: Double,
    val predicted_class: String
)

interface ApiService {
    @Multipart
    @POST("/predict")
    fun predict(
        @Part image: MultipartBody.Part
    ): Call<PredictResponse>
}
