package net.dimterex.sync_client.modules

import android.util.Log
import net.dimterex.sync_client.modules.Executors.Transport.IRestApi
import net.dimterex.sync_client.modules.Executors.Transport.rest.RestClientBuilder
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import kotlin.reflect.KFunction1

interface ConnectionManager {

    var isConnected: Boolean
    fun raiseConnection()
    fun subscribe_connection_state_change_event(connectedStateChangeFunc: KFunction1<Boolean, Unit>)
    fun restart_connection()
    fun setToken(token: String)

    suspend fun download(name: List<String>): Response<ResponseBody>
    suspend fun upload(fileName: List<String>, fileRequestBody: RequestBody): Response<ResponseBody>
    suspend fun send_request(request: RequestBody): Response<ResponseBody>
    suspend fun send_connection_request(request: RequestBody): Response<ResponseBody>
    fun unsubscribe_connection_state_change_event(kFunction1: KFunction1<Boolean, Unit>)


    class Impl(private val settingsManager: SettingsManager,
               private val _restClientBuilder: RestClientBuilder
    ) : ConnectionManager {

        private val TAG = this::class.java.name

        private val _connectedStateChangeFuncs: ArrayList<KFunction1<Boolean, Unit>>

        private var _restApi: IRestApi? = null
        private var _token: String = String()
        override var isConnected: Boolean = false


        init {
            settingsManager.add_listener(this::restart_connection)
            _connectedStateChangeFuncs = ArrayList()
        }

        private fun interrupt() {
            setToken(String())
        }

        override suspend fun download(name: List<String>): Response<ResponseBody> {
            val test = JSONObject()
            test.putOpt("path", JSONArray(name))
            val rawJson = test.toString()
            return _restApi!!.download(_token, rawJson)
        }

        override fun setToken(token: String) {
            _token = token
        }

        override suspend fun upload(fileName: List<String>, fileRequestBody: RequestBody): Response<ResponseBody> {
            val test = JSONObject()
            test.putOpt("path", JSONArray(fileName))
            val rawJson = test.toString()
            return _restApi!!.upload(_token, rawJson, fileRequestBody)
        }

        override suspend fun send_request(request: RequestBody): Response<ResponseBody> {
            return _restApi!!.sync(_token,  request)
        }

        override suspend fun send_connection_request(request: RequestBody): Response<ResponseBody> {
            return _restApi!!.connection(_token,  request)
        }

        private fun connect()
        {
            try {
                interrupt()
                val connectionsLocalModel = settingsManager.get_connection_settings()

                Log.i(TAG, "${connectionsLocalModel.ip_address}:${connectionsLocalModel.ip_port}")
                _restApi = _restClientBuilder.createService("${connectionsLocalModel.ip_address}:${connectionsLocalModel.ip_port}", false)

            } catch (e: Exception) {
                println(e.toString())
                interrupt()
            }
        }

        override fun subscribe_connection_state_change_event(connectedStateChangeFunc: KFunction1<Boolean, Unit>) {
            _connectedStateChangeFuncs.add(connectedStateChangeFunc)
        }

        override fun unsubscribe_connection_state_change_event(connectedStateChangeFunc: KFunction1<Boolean, Unit>) {
            _connectedStateChangeFuncs.remove(connectedStateChangeFunc)
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



