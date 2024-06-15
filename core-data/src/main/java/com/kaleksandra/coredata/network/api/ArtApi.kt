package com.kaleksandra.coredata.network.api

import com.kaleksandra.coredata.network.model.ImageStatsDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ArtApi {
    @Multipart
    @POST("art/predict")
    suspend fun sendImage(
        @Part image: MultipartBody.Part,
    ): Response<ImageStatsDTO>
}