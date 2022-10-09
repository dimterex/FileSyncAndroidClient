package net.dimterex.sync_client.modules.Executors.Transport

import net.dimterex.sync_client.api.Message.Sync.FileInfoItem
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface IRestApi {

    @Streaming
    @POST("/api/files/0/upload")
    suspend fun upload(
        @Query("token") token: String,
        @Query("file_name") fileName: String,
        @Body fileRequestBody: RequestBody
    ): Response<ResponseBody>

    @Streaming
    @GET("/api/files/0/download")
    suspend fun download(
        @Query("token") token: String,
        @Query("file_id") fileId: String
    ): Response<ResponseBody>

    @Streaming
    @POST("/api/core/0/request")
    suspend fun sync(
        @Query("token") token: String,
        @Body filesRequestBody: RequestBody
    ): Response<ResponseBody>
}
