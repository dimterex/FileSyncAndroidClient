package net.dimterex.sync_client.modules.Executors.Transport

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface IAttachmentRestApi {

    @Streaming
    @POST("/api/attachment/0/upload")
    suspend fun upload(
        @Query("token") token: String,
        @Query("file_name") fileName: String,
        @Body fileRequestBody: RequestBody
    ): Response<ResponseBody>

    @Streaming
    @GET("/api/attachment/0/download")
    suspend fun download(
        @Query("token") token: String,
        @Query("file_id") fileId: String?
    ): Response<ResponseBody>

    @Streaming
    @GET("/api/sync/0/sync")
    suspend fun sync(
        @Query("token") token: String,
        @Query("files") files: String
    ): Response<ResponseBody>
}
