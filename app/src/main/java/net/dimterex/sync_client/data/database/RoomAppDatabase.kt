package net.dimterex.sync_client.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.dimterex.sync_client.BuildConfig
import net.dimterex.sync_client.data.settings.local.SettingsDao
import net.dimterex.sync_client.data.settings.local.SettingsLocalModel

@Database(entities = [SettingsLocalModel::class], version = BuildConfig.DATABASE_VERSION)
abstract class RoomAppDatabase : RoomDatabase() {

    abstract fun settingsDao(): SettingsDao
}