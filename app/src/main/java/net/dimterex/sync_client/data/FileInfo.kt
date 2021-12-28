package net.dimterex.sync_client.data

import net.dimterex.sync_client.entity.FileSyncType

data class FileInfo(
    val name: String,
    val type: FileSyncType,
    val sizeBytes: Long = 0
)
