package net.dimterex.sync_client.di

import android.content.Context
import net.dimterex.sync_client.api.Modules.ConnectionApi
import net.dimterex.sync_client.api.Modules.SyncApi
import net.dimterex.sync_client.modules.Executors.Sync.*
import net.dimterex.sync_client.modules.Executors.Connection.ConnectionRequestExecutor
import net.dimterex.sync_client.modules.Executors.Connection.ConnectionResponseExecutor
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.eagerSingleton
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

fun executorsModule(appContext: Context) = Kodein.Module("executors") {

    bind<SyncStateFilesRequestExecutor>() with singleton  { SyncStateFilesRequestExecutor(instance(), instance(), instance(), instance()) }
    bind<SyncStateFilesResponseExecutor>() with singleton  { SyncStateFilesResponseExecutor(instance()) }
    bind<SyncStartFilesRequestExecutor>() with singleton  { SyncStartFilesRequestExecutor(instance(), instance()) }
    bind<SyncStartFilesResponseExecutor>() with singleton  { SyncStartFilesResponseExecutor(instance(), instance(), instance(), instance(), instance(), instance()) }
    bind<SyncApi>() with eagerSingleton  { SyncApi(instance(), instance(), instance(), instance(), instance()) }


    bind<ConnectionRequestExecutor>() with singleton  { ConnectionRequestExecutor(instance(), instance(), instance(),instance()) }
    bind<ConnectionResponseExecutor>() with singleton  { ConnectionResponseExecutor(instance(), instance(), instance()) }
    bind<ConnectionApi>() with eagerSingleton  { ConnectionApi(instance(), instance(), instance()) }
}