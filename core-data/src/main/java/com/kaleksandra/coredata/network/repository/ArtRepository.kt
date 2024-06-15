package com.kaleksandra.coredata.network.repository

import com.kaleksandra.coredata.network.Effect
import com.kaleksandra.coredata.network.api.ArtApi
import com.kaleksandra.coredata.network.call
import com.kaleksandra.coredata.network.di.IoDispatcher
import com.kaleksandra.coredata.network.model.ImageStatsDTO
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

private const val MULTIPART_FORMAT: String = "multipart/form-data"
private const val MULTIPART_NAME: String = "file"

interface ArtRepository {
    suspend fun sendPhoto(file: File): Effect<ImageStatsDTO>
}

class ArtRepositoryImpl @Inject constructor(
    val api: ArtApi,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : ArtRepository {
    override suspend fun sendPhoto(file: File): Effect<ImageStatsDTO> {
        val multipartBody = MultipartBody.Part.createFormData(
            MULTIPART_NAME,
            file.name,
            file.asRequestBody(MULTIPART_FORMAT.toMediaTypeOrNull())
        )
        return call(dispatcher) { api.sendImage(multipartBody) }
    }
}