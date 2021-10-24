package net.dimterex.sync_client.modules

import android.util.Log
import net.dimterex.sync_client.modules.Executors.Transport.IRestApi
import net.dimterex.sync_client.modules.Executors.Transport.rest.RestClientBuilder
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.reflect.KFunction1

interface ConnectionManager {

    var isConnected: Boolean
    fun raiseConnection()
    fun addMessageReceivedListener(messageReceived: KFunction1<String, Unit>)
    fun addConnectionStateListener(connectedStateChangeFunc: KFunction1<Boolean, Unit>)
    fun restart_connection()

    fun setToken(token: String)


    suspend fun download(name: String): Response<ResponseBody>
    suspend fun upload(fileName: String, fileRequestBody: RequestBody): Response<ResponseBody>
    suspend fun send_request(request: RequestBody): Response<ResponseBody>


    class Impl(private val settingsManager: SettingsManager,
               private val _restClientBuilder: RestClientBuilder
    ) : ConnectionManager {

        private val TAG = this::class.java.name

        private var _messageReceivedFunc: KFunction1<String, Unit>? = null
        private val _connectedStateChangeFuncs: ArrayList<KFunction1<Boolean, Unit>>

        private var _downloadService: IRestApi? = null
        private var _token: String = String()
        override var isConnected: Boolean = false


        init {
            settingsManager.add_listener(this::restart_connection)
            _connectedStateChangeFuncs = ArrayList()
        }

        private fun interrupt() {
            setToken(String())
        }


        override suspend fun download(name: String): Response<ResponseBody> {
            return _downloadService!!.download(_token, name)
        }

        override fun setToken(token: String) {
            _token = token
        }

        override suspend fun upload(fileName: String, fileRequestBody: RequestBody): Response<ResponseBody> {
            return _downloadService!!.upload(_token, fileName, fileRequestBody)
        }

        override suspend fun send_request(request: RequestBody): Response<ResponseBody> {
            return _downloadService!!.sync(_token,  request)
        }

        private fun connect()
        {
            try {
                interrupt()
                val connectionsLocalModel = settingsManager.get_connection_settings()

                Log.i(TAG, "${connectionsLocalModel.ip_address}:${connectionsLocalModel.ip_port}")
                _downloadService = _restClientBuilder.createService("${connectionsLocalModel.ip_address}:${connectionsLocalModel.ip_port}", false)

            } catch (e: Exception) {
                println(e.toString())
                interrupt()
            }
        }


        override fun addMessageReceivedListener(messageReceived: KFunction1<String, Unit>) {
            _messageReceivedFunc = messageReceived
        }

        override fun addConnectionStateListener(connectedStateChangeFunc: KFunction1<Boolean, Unit>) {
            _connectedStateChangeFuncs.add(connectedStateChangeFunc)
        }

        override fun restart_connection() {
            connect()
        }

        override fun raiseConnection() {
            isConnected = true
            _connectedStateChangeFuncs.forEach{ x ->
                x.invoke(true)
            }
        }
    }
}



