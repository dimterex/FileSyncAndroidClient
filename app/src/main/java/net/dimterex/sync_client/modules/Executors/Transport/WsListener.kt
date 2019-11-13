package net.dimterex.sync_client.modules.Executors.Transport

import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFrame
import kotlin.reflect.KFunction1

class WsListener(
    val messageReceavedFunc: KFunction1<String, Unit>?,
    val client: WebSocket
) : WebSocketAdapter() {
    override fun onTextMessage(websocket: WebSocket, text: String) {
        messageReceavedFunc?.invoke(text)
    }

    override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
        client.recreate()
    }

    override fun handleCallbackError(websocket: WebSocket?, cause: Throwable?) {

    }

    override fun onCloseFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onDisconnected(websocket: WebSocket?, serverCloseFrame: WebSocketFrame?,
                                clientCloseFrame: WebSocketFrame?, closedByServer: Boolean) {
    }
}