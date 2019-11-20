package net.dimterex.sync_client.api.Modules.Common

import net.dimterex.sync_client.modules.ExecuteManager


open class BaseApiModule (open val executeManager: ExecuteManager){

    fun Init() {
        RegisterHandlers()
    }

    open fun RegisterHandlers() {}
}
