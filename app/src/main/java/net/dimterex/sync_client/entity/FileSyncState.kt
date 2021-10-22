package net.dimterex.sync_client.entity

data class FileSyncState (
    val id: String,
    val state: FileSyncType,
    val number: String,
    var details: Int = 0
)