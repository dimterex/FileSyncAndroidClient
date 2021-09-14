package net.dimterex.sync_client.modules.Executors.Transport

import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction0

class WsClient(url: URI, val messageReceavedFunc: KFunction1<String, Unit>?, val onOpenFunc: KFunction0<Unit>?)
    : WebSocketClient(url, Draft_6455()) {

    init {

    }

    override fun onOpen(handshakedata: ServerHandshake) {
        println("new connection opened")
        onOpenFunc?.invoke()
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        println("closed with exit code $code additional info: $reason")
    }

    override fun onMessage(message: String) {
        messageReceavedFunc?.invoke(message)
//        println("received message: $message")
    }


    override fun onError(ex: Exception) {
        println("an error occurred:$ex")
    }

}