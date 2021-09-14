package net.dimterex.sync_client.data.entries

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folder_mapping")
data class FolderMappingLocalModel (
    @PrimaryKey @ColumnInfo(name = "inside_folder")  val inside_folder: String,
    @ColumnInfo(name = "outside_folder") var outside_folder: String
)