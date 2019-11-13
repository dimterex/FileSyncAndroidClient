package net.dimterex.sync_client.modules

import net.dimterex.sync_client.api.Modules.ActionApi
import net.dimterex.sync_client.api.Modules.InfoApi

interface ApiModules {

    fun initialize()

    class Impl(private val _fileManager: FileManager, private val _executeManager: ExecuteManager,
               private val _eventLoggerManager: EventLoggerManager) : ApiModules {

        override fun initialize() {
            InfoApi(_fileManager, _executeManager, _eventLoggerManager).Init()
            ActionApi(_fileManager, _executeManager, _eventLoggerManager).Init()
        }
    }
}