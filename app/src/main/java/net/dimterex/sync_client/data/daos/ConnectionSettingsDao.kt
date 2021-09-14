package net.dimterex.sync_client.data.daos

import androidx.room.*
import net.dimterex.sync_client.data.entries.ConnectionsLocalModel

@Dao
interface ConnectionSettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: ConnectionsLocalModel)

    @Query("SELECT * FROM connection_settings WHERE id = :id")
    fun selectById(id: Int): ConnectionsLocalModel

    @Update
    fun update(settingsLocalModel: ConnectionsLocalModel)
}