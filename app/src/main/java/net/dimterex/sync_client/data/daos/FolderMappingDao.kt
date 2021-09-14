package net.dimterex.sync_client.data.daos

import androidx.room.*
import net.dimterex.sync_client.data.entries.FolderMappingLocalModel

@Dao
interface FolderMappingDao {

    @Query("SELECT * FROM folder_mapping")
    fun getAll(): List<FolderMappingLocalModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(folderMappingLocalModel: FolderMappingLocalModel)

    @Update
    fun update(folderMappingLocalModel: FolderMappingLocalModel)

    @Delete
    fun delete(folderMappingLocalModel: FolderMappingLocalModel)
}