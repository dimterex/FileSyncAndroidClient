package net.dimterex.sync_client.modules

import net.dimterex.sync_client.api.Modules.ActionApi
import net.dimterex.sync_client.api.Modules.ConnectionApi
import net.dimterex.sync_client.api.Modules.InfoApi

interface ApiModules {

    class Impl(private val _fileManager: FileManager,
               private val _executeManager: ExecuteManager,
               private val _eventLoggerManager: EventLoggerManager,
               private val _connectionManager: ConnectionManager
    ) : ApiModules {

        init {
            ConnectionApi(_executeManager).Init()
            InfoApi(_fileManager, _executeManager, _eventLoggerManager).Init()
            ActionApi(_fileManager, _executeManager, _eventLoggerManager, _connectionManager).Init()
        }
    }
}