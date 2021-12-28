package net.dimterex.sync_client.ui.formatter

import android.content.res.Resources
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.FileSyncType

class FileSyncTypeFormatter (private val _resources: Resources) {
    fun format(fileSyncType: FileSyncType): String {

        if (fileSyncType == FileSyncType.DOWNLOAD)
            return _resources.getString(R.string.download)

        if (fileSyncType == FileSyncType.UPLOAD)
            return _resources.getString(R.string.upload)

        if (fileSyncType == FileSyncType.DELETE)
            return _resources.getString(R.string.remove)

        return "$fileSyncType"
    }
}