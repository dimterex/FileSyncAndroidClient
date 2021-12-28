package net.dimterex.sync_client.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import net.dimterex.sync_client.BuildConfig
import net.dimterex.sync_client.data.daos.ConnectionSettingsDao
import net.dimterex.sync_client.data.daos.FolderMappingDao
import net.dimterex.sync_client.data.entries.ConnectionsLocalModel
import net.dimterex.sync_client.data.entries.FolderMappingLocalModel

@Database(entities = [ConnectionsLocalModel::class, FolderMappingLocalModel::class], version = BuildConfig.DATABASE_VERSION)
abstract class RoomAppDatabase : RoomDatabase() {

    abstract fun folderMappingDao(): FolderMappingDao

    abstract fun connectionSettingsDao(): ConnectionSettingsDao
}