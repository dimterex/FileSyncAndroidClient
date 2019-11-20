package net.dimterex.sync_client.modules

import net.dimterex.sync_client.entity.ConnectionSettings
import net.dimterex.sync_client.modules.Executors.Transport.TcpClient
import net.dimterex.sync_client.modules.Executors.Transport.WsClient
import java.net.InetSocketAddress
import java.net.URI
import kotlin.reflect.KFunction1

interface ConnectionManager {

    fun addListener(function: KFunction1<String, Unit>)
    fun start()
    fun send(raw_string: String)
    fun add_event_listener(function: KFunction1<String, Unit>)

    class Impl(private val settingsManager:SettingsManager) : ConnectionManager, Thread() {

        private var _messageReceavedFunc: KFunction1<String, Unit>? = null
        private var _event_listener: KFunction1<String, Unit>? = null

        private var _client : TcpClient? = null
//        private var _client : WsClient? = null

        private var _connectionSettings: ConnectionSettings = settingsManager.get_settings().connectionSettings

        init {
            settingsManager.add_listener(this::restart_connection)
        }

        override fun run() {
            connect()
        }

        override fun interrupt() {
            _client?.close()
        }

        override fun send(raw_string: String){
            write(raw_string)
        }

        override fun add_event_listener(function: KFunction1<String, Unit>) {
            _event_listener = function
        }

        override fun addListener(function: KFunction1<String, Unit>) {
            _messageReceavedFunc = function
        }

        private fun connect()
        {
            try {
                interrupt()

                var addr  = InetSocketAddress(_connectionSettings.ip_address, _connectionSettings.port)

                _client = TcpClient(addr, _messageReceavedFunc)
//                _client = WsClient(URI("ws://${_connectionSettings.ip_address}:${_connectionSettings.port}"), _messageReceavedFunc)
//                _client?.connect()

            } catch (e: Exception) {
                _event_listener?.invoke(e.toString())
                interrupt()
            }
        }

        private fun write(message: String) {
            _client?.send(message)
        }

        private fun restart_connection() {
            connect()
        }
    }
}



