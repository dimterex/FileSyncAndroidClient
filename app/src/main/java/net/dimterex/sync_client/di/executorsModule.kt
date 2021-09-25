package net.dimterex.sync_client.di

import net.dimterex.sync_client.api.Message.Action.AddFileResponce
import net.dimterex.sync_client.api.Message.Action.RemoveFileResponce
import net.dimterex.sync_client.api.Message.Action.SyncFilesRequest
import net.dimterex.sync_client.api.Message.Connection.ConnectionRequest
import net.dimterex.sync_client.api.Message.Connection.ConnectionResponse
import net.dimterex.sync_client.api.Modules.ActionApi
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.api.Modules.ConnectionApi
import net.dimterex.sync_client.api.Modules.InfoApi
import net.dimterex.sync_client.modules.*
import net.dimterex.sync_client.modules.Executors.Action.AddFileResponseExecutor
import net.dimterex.sync_client.modules.Executors.Action.FileUploadRequestExecutor
import net.dimterex.sync_client.modules.Executors.Action.RemoveFileResponseExecutor
import net.dimterex.sync_client.modules.Executors.Action.SyncFilesRequestExecutor
import net.dimterex.sync_client.modules.Executors.Connection.ConnectionRequestExecutor
import net.dimterex.sync_client.modules.Executors.Connection.ConnectionResponseExecutor
import net.dimterex.sync_client.modules.Executors.Transport.rest.RestClientBuilder
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.eagerSingleton
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

val executorsModule = Kodein.Module("executors") {

    bind<AddFileResponseExecutor>() with singleton { AddFileResponseExecutor(instance(), instance(), instance(), instance()) }
    bind<FileUploadRequestExecutor>() with singleton { FileUploadRequestExecutor(instance(), instance(), instance(), instance()) }
    bind<RemoveFileResponseExecutor>() with singleton { RemoveFileResponseExecutor(instance(), instance(), instance()) }
    bind<ActionApi>() with eagerSingleton  { ActionApi(instance(), instance(), instance(), instance()) }


    bind<SyncFilesRequestExecutor>() with singleton  { SyncFilesRequestExecutor(instance(), instance()) }
    bind<InfoApi>() with eagerSingleton  { InfoApi(instance(), instance()) }


    bind<ConnectionRequestExecutor>() with singleton  { ConnectionRequestExecutor() }
    bind<ConnectionResponseExecutor>() with singleton  { ConnectionResponseExecutor(instance(), instance()) }
    bind<ConnectionApi>() with eagerSingleton  { ConnectionApi(instance(), instance(), instance()) }


}