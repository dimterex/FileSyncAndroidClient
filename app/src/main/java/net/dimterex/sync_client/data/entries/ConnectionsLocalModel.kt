package net.dimterex.sync_client.data.entries

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "connection_settings")
data class ConnectionsLocalModel(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "ip_address") var ip_address: String,
    @ColumnInfo(name = "ip_port") var ip_port: Int,
    @ColumnInfo(name = "login") var login: String,
    @ColumnInfo(name = "password") var password: String
)