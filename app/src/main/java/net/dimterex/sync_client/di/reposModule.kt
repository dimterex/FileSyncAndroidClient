package net.dimterex.sync_client.di

import net.dimterex.sync_client.data.database.RoomAppDatabase
import net.dimterex.sync_client.data.settings.SettingsDaoImpl
import net.dimterex.sync_client.data.settings.local.SettingsDao
import net.dimterex.sync_client.modules.*
import net.dimterex.sync_client.usecase.repos.*
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton

val repoModule = Kodein.Module("repo") {

    bind<SettingsDao>() with singleton { instance<RoomAppDatabase>().settingsDao() }

    bind<ISettingsSource>() with singleton { SettingsDaoImpl(instance()) }

    bind<SettingsManager>() with singleton { SettingsManager.Impl(instance()) }

    bind<ConnectionManager>() with singleton { ConnectionManager.Impl(instance()) }

    bind<JsonManager>() with singleton { JsonManager.Impl(instance()) }

    bind<FileManager>() with singleton { FileManager.Impl(instance()) }

    bind<ExecuteManager>() with singleton { ExecuteManager.Impl(instance()) }

    bind<ApiModules>() with singleton { ApiModules.Impl(instance(), instance(), instance()) }

    bind<EventLoggerManager>() with singleton { EventLoggerManager.Impl(instance()) }
}