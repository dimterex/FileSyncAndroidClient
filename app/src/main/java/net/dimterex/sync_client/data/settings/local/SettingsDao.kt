package net.dimterex.sync_client.data.settings.local

import androidx.room.*

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(repo: SettingsLocalModel)

    @Query("SELECT * FROM settings WHERE id = :id")
    fun selectById(id: Int): SettingsLocalModel?

    @Update
    fun update(settingsLocalModel: SettingsLocalModel)
}