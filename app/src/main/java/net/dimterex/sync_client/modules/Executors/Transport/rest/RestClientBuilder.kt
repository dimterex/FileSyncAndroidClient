package net.dimterex.sync_client.modules.Executors.Transport.rest

import net.dimterex.sync_client.modules.Executors.Transport.IAttachmentRestApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RestClientBuilder {

    fun createService(baseUrl: String, useSsl: Boolean): IAttachmentRestApi {
        val client = getUnsafeOkHttpClient() //TODO REMOVE ME!!!

        return Retrofit.Builder().apply {
            if (useSsl)
                baseUrl("https://$baseUrl")
            else
                baseUrl("http://$baseUrl")
        }
            .client(client)
            .build()
            .create(IAttachmentRestApi::class.java)
    }

    fun getUnsafeOkHttpClient(): OkHttpClient {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) = Unit

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) = Unit

                    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
                }
            )
            // Install the all-trusting trust manager
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(
                sslSocketFactory,
                trustAllCerts[0] as X509TrustManager
            )
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })
            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}