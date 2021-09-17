package net.dimterex.sync_client.modules

import net.dimterex.sync_client.api.Message.Action.AddFileResponce
import net.dimterex.sync_client.api.Modules.ActionApi
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.Modules.ConnectionApi
import net.dimterex.sync_client.api.Modules.InfoApi
import net.dimterex.sync_client.data.ScopeFactory

interface ApiModules {

    class Impl(private val _fileManager: FileManager,
               private val _executeManager: ExecuteManager,
               private val _FileState_eventManager: FileStateEventManager,
               private val _infoApi: InfoApi,
               private val _actionApi: ActionApi
    ) : ApiModules {

        init {
//            ConnectionApi(_executeManager, _FileState_eventManager).Init()
//            _infoApi.Init()
//            _actionApi.Init()
        }
    }
}