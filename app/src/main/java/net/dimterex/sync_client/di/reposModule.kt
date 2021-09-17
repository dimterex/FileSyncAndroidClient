package net.dimterex.sync_client.di

import net.dimterex.sync_client.api.Modules.ActionApi
import net.dimterex.sync_client.modules.*
import net.dimterex.sync_client.modules.Executors.Transport.rest.RestClientBuilder
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.eagerSingleton
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

val repoModule = Kodein.Module("repo") {

    bind<SettingsManager>() with singleton { SettingsManager.Impl(instance(), instance()) }

    bind<ConnectionManager>() with singleton { ConnectionManager.Impl(instance(), instance()) }

    bind<JsonManager>() with singleton { JsonManager.Impl(instance(), instance()) }

    bind<FileManager>() with singleton { FileManager.Impl(instance()) }

    bind<ExecuteManager>() with singleton { ExecuteManager.Impl(instance()) }


//    bind<ApiModules>() with eagerSingleton  { ApiModules.Impl(instance(), instance(), instance(),  instance(), instance()) }

    bind<FileStateEventManager>() with singleton { FileStateEventManager.Impl(instance()) }

    bind<RestClientBuilder>() with singleton { RestClientBuilder()  }
}