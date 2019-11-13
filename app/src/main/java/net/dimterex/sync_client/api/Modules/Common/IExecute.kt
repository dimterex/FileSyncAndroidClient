package net.dimterex.sync_client.api.Modules.Common

import net.dimterex.sync_client.api.interfaces.IMessage

interface IExecute <T : IMessage>{
    fun Execute(param: T)
}