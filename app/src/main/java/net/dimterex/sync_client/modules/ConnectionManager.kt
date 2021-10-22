package net.dimterex.sync_client.modules

import android.util.Log
import net.dimterex.sync_client.modules.Executors.Transport.IAttachmentRestApi
import net.dimterex.sync_client.modules.Executors.Transport.WsClient
import net.dimterex.sync_client.modules.Executors.Transport.rest.RestClientBuilder
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.URI
import kotlin.reflect.KFunction1

interface ConnectionManager {

    var isConnected: Boolean;
    fun addMessageReceivedListener(messageReceived: KFunction1<String, Unit>)
    fun addConnectionStateListener(connectedStateChangeFunc: KFunction1<Boolean, Unit>)
    fun restart_connection()
    fun send(raw_string: String)
    suspend fun download(name: String): Response<ResponseBody>
    fun setToken(token: String)
    suspend fun upload(fileName: String, fileRequestBody: RequestBody): Response<ResponseBody>

    suspend fun sync(request: Array<String>): Response<ResponseBody>


    class Impl(private val settingsManager:SettingsManager,
               private val _restClientBuilder: RestClientBuilder
    ) : ConnectionManager {

        private val TAG = this::class.java.name

        private var _messageReceivedFunc: KFunction1<String, Unit>? = null
        private val _connectedStateChangeFuncs: ArrayList<KFunction1<Boolean, Unit>>

        private var _client : WsClient? = null
        private var _downloadService: IAttachmentRestApi? = null
        override var isConnected: Boolean = false
        private var _token: String = String()


        init {
            settingsManager.add_listener(this::restart_connection)
            _connectedStateChangeFuncs = ArrayList()
        }

        private fun interrupt() {
            setToken(String())
            _client?.close()
            _client = null
        }

        override fun send(raw_string: String){
            write(raw_string)
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

        override suspend fun sync(request: Array<String>): Response<ResponseBody> {
            return _downloadService!!.sync(_token,  request.joinToString(";"))
        }

        private fun connect()
        {
            try {
                interrupt()
                val connectionsLocalModel = settingsManager.get_connection_settings()

                Log.i(TAG, "${connectionsLocalModel.ip_address}:${connectionsLocalModel.ip_port}")
                _downloadService = _restClientBuilder.createService("${connectionsLocalModel.ip_address}:${connectionsLocalModel.ip_port}", false)

                _client = WsClient(URI("ws://${connectionsLocalModel.ip_address}:${connectionsLocalModel.ip_port}"), _messageReceivedFunc, this::connectStateChange)
                _client!!.connect()

            } catch (e: Exception) {
                println(e.toString())
                interrupt()
            }
        }

        private fun write(message: String) {
            if (isConnected)
                _client!!.send(message)
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

        private fun connectStateChange(isConnect: Boolean) {
            isConnected = isConnect
            _connectedStateChangeFuncs.forEach{ x ->
                x.invoke(isConnected)
            }
        }
    }
}



