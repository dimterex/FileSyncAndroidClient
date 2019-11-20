package net.dimterex.sync_client.api.Message.Temp

import net.dimterex.sync_client.api.Message.Action.AddFileResponce
import net.dimterex.sync_client.api.MessageAttr

@MessageAttr(name = "SaveFileRequest")
class SaveFileRequest() : AddFileResponce() {
}