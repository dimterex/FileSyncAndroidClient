package net.dimterex.sync_client.entity

data class FileSyncState (
    val inside_path: String,
    val outside_path: String,
    val state: FileSyncType,
    val number: String,
    var process: Int
)