package net.dimterex.sync_client.modules

import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.modules.Executors.Transport.IAttachmentRestApi
import net.dimterex.sync_client.modules.Executors.Transport.WsClient
import net.dimterex.sync_client.modules.Executors.Transport.rest.RestClientBuilder
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.URI
import kotlin.reflect.KFunction1

interface ConnectionManager {

    fun addListener(messageReceived: KFunction1<String, Unit>, connectedStateChangeFunc: KFunction1<Boolean, Unit>)
    fun send(raw_string: String)
    fun add_event_listener(function: KFunction1<FileSyncState, Unit>)

    suspend fun download(name: String): Response<ResponseBody>

    class Impl(private val settingsManager:SettingsManager,
               private val _restClientBuilder: RestClientBuilder
    ) : ConnectionManager {

        private var _messageReceavedFunc: KFunction1<String, Unit>? = null
        private var _event_listener: KFunction1<FileSyncState, Unit>? = null
        private var _connectedStateChangeFunc: KFunction1<Boolean, Unit>? = null

        private var _client : WsClient? = null
        private var _downloadService: IAttachmentRestApi? = null


        init {
            settingsManager.add_listener(this::restart_connection)
        }

        private fun interrupt() {
            _client?.close()
        }

        override fun send(raw_string: String){
            write(raw_string)
        }

        override fun add_event_listener(function: KFunction1<FileSyncState, Unit>) {
            _event_listener = function
        }

        override suspend fun download(name: String): Response<ResponseBody> {
            return _downloadService!!.download(name)
        }

        override fun addListener(messageReceived: KFunction1<String, Unit>, connectedStateChangeFunc: KFunction1<Boolean, Unit>) {
            _messageReceavedFunc = messageReceived
            _connectedStateChangeFunc = connectedStateChangeFunc
        }

        private fun connect()
        {
            try {
                interrupt()
                var connectionsLocalModel = settingsManager.get_connection_settings()

                _downloadService = _restClientBuilder.createService("${connectionsLocalModel.ip_address}:${connectionsLocalModel.ip_port}", false)

                _client = WsClient(URI("ws://${connectionsLocalModel.ip_address}:${connectionsLocalModel.ip_port}"), _messageReceavedFunc, this::connectStateChange)
                _client?.connect()

            } catch (e: Exception) {
                println(e.toString())
                interrupt()
            }
        }

        private fun write(message: String) {
            _client?.send(message)
        }

        private fun restart_connection() {
            connect()
        }

        private fun connectStateChange(isConnected: Boolean) {
            _connectedStateChangeFunc?.invoke(isConnected)
        }
    }
}



