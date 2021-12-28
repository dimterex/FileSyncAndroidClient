package net.dimterex.sync_client.modules.Executors.Connection

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.dimterex.sync_client.api.Message.Connection.ConnectionRequest
import net.dimterex.sync_client.api.Message.Connection.ConnectionResponse
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.modules.*

class ConnectionRequestExecutor(
    private val _jsonManager: JsonManager,
    private val _settingsManager: SettingsManager,
    private val _executerManager: ExecuteManager,

    private val _scopeFactory: ScopeFactory
) : IExecute<ConnectionRequest> {

    private val TAG = this::class.java.name

    private val scope: CoroutineScope = _scopeFactory.getScope()
    init {
        _settingsManager.add_listener(this::restart_connection)
    }

    private fun restart_connection() {
        _executerManager.execute(ConnectionRequest())
    }

    override fun Execute(connectionRequest: ConnectionRequest) {
        connectionRequest.login = _settingsManager.get_connection_settings().login
        connectionRequest.password = _settingsManager.get_connection_settings().password
        startProcessing(connectionRequest)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    fun startProcessing(param: ConnectionRequest) {

        scope.launch {
            try {
                val response = _jsonManager.getPostMessage(param)
                if (!response.isSuccessful)
                    throw Exception("File not found!")

                val inputStream = response.body()?.string()

                _jsonManager.restResponse(inputStream?:String(), ConnectionResponse::class.java)
            } catch (e: Throwable) {
                println(e)
                e.printStackTrace()
            }
        }
    }
}