package net.dimterex.sync_client.data.settings.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.dimterex.sync_client.data.DataMapper
import net.dimterex.sync_client.entity.Settings

@Entity(tableName = "settings")
data class SettingsLocalModel(
    @PrimaryKey
    val id: Long,
    val sync_folder: String,
    val ip_adress: String,
    val ip_port: Int
) : DataMapper<Settings> {

    override fun mapToDomain(): Settings{
        var result = Settings()
        result.defaultFolder = sync_folder
        result.connectionSettings.ip_address = ip_adress
        result.connectionSettings.port = ip_port
        return result
    }
}

fun Settings.toLocalModel() = SettingsLocalModel(0,
    this.defaultFolder,
    this.connectionSettings.ip_address,
    this.connectionSettings.port)