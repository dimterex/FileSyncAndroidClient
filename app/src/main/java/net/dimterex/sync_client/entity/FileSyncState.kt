package net.dimterex.sync_client.entity

data class FileSyncState (
    val id: String,
    val state: FileSyncType,
    var details: Int = 0
)