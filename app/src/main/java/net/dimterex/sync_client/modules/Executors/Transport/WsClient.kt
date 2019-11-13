package net.dimterex.sync_client.modules.Executors.Transport

import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.nio.ByteBuffer
import kotlin.reflect.KFunction1

class WsClient(url: URI, val messageReceavedFunc: KFunction1<String, Unit>?)
    : WebSocketClient(url, Draft_6455()) {

    init {

    }

    override fun onOpen(handshakedata: ServerHandshake) {
//        send("Hello, it is me. Mario :)")
        println("new connection opened")
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        println("closed with exit code $code additional info: $reason")
    }

    override fun onMessage(message: String) {
//        messageReceavedFunc?.invoke(message)
//        println("received message: $message")
    }

    override fun onMessage(message: ByteBuffer) {
//        println("received ByteBuffer")
    }

    override fun onError(ex: Exception) {
        System.err.println("an error occurred:$ex")
    }

}