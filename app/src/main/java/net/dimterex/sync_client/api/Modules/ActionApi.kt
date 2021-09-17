package net.dimterex.sync_client.api.Modules

import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.Message.Action.AddFileResponce
import net.dimterex.sync_client.api.Message.Action.RemoveFileResponce
import net.dimterex.sync_client.modules.*
import net.dimterex.sync_client.modules.Executors.Action.AddFileResponseExecutor
import net.dimterex.sync_client.modules.Executors.Action.RemoveFileResponseExecutor

class ActionApi(_executeManager: ExecuteManager,
                _addFileResponseExecutor: AddFileResponseExecutor,
                _removeFileResponseExecutor: RemoveFileResponseExecutor)
{

    init {
        _executeManager.initApiMessage(AddFileResponce::class.java, _addFileResponseExecutor as IExecute<IMessage>)
        _executeManager.initApiMessage(RemoveFileResponce::class.java, _removeFileResponseExecutor as IExecute<IMessage>)
    }
}