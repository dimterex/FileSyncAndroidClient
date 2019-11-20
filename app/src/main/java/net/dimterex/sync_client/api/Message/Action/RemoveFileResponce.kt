package net.dimterex.sync_client.api.Message.Action

import net.dimterex.sync_client.api.Message.Common.BaseFileInfo
import net.dimterex.sync_client.api.MessageAttr

@MessageAttr(name = "FileRemoveResponce")
class RemoveFileResponce() : BaseFileInfo() {}