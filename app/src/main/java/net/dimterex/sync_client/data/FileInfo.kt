package net.dimterex.sync_client.data

import android.net.Uri

data class FileInfo(
    val name: String,
    val uri: Uri? = null,
    val sizeBytes: Long = 0
)
