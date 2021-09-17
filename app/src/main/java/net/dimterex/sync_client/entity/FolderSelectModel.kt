package net.dimterex.sync_client.entity

import net.dimterex.sync_client.data.entries.FolderMappingLocalModel

class FolderSelectModel(
    val folFolderMappingLocalModel: FolderMappingLocalModel,
    var folders: Array<String>
) {

}