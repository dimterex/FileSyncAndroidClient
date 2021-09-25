package net.dimterex.sync_client.modules

import com.google.gson.Gson
import com.google.gson.JsonParser
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.MessageAttr
import net.dimterex.sync_client.api.Message.MessageContainer
import java.lang.reflect.Type
import java.util.HashMap
import kotlin.reflect.KFunction1
import net.dimterex.sync_client.api.Message.Connection.ConnectionRequest


interface JsonManager {

    fun addListener(function: KFunction1<IMessage, Unit>)

    fun <T: Any> initApiMessage(classType: Class<T>)

    fun sendMessage(iMessage: IMessage)

    class Impl(private val _connection: ConnectionManager,
               private val _settingsManager: SettingsManager) : JsonManager {
        private val _gson : Gson = Gson()
        private val _messageEnc : HashMap<Type, String> = HashMap()
        private val _messageDec : HashMap<String, Type> = HashMap()

        private var _messageReceivedFunc: KFunction1<IMessage, Unit>? = null

        init {
            _connection.addMessageReceivedListener(this::messageReceivedListener)
            _connection.addConnectionStateListener(this::onOpenFunc)
        }

        override fun addListener(function: KFunction1<IMessage, Unit>) {
            _messageReceivedFunc = function
        }

        private fun messageReceivedListener(raw_string: String)
        {
            var messages = _gson.fromJson(raw_string, Array<MessageContainer>::class.java)
            for (message in messages)
            {
                var result = deserialize(message)
                if (result != null)
                    _messageReceivedFunc?.invoke(result)
            }
        }

        override fun <T: Any> initApiMessage(classType: Class<T>) {
            val messageAttr = classType.getAnnotation(MessageAttr::class.java) ?: return
            val id = messageAttr.name

            _messageDec[id]= classType
            _messageEnc[classType] = id
        }

        override fun sendMessage(iMessage: IMessage) {
            val msgArr = ArrayList<MessageContainer>()
            val msg: MessageContainer = serialize(iMessage) ?: return
            msgArr.add(msg)
            val result = _gson.toJson(msgArr)
            _connection.send(result)
        }

        private fun serialize(message: IMessage): MessageContainer? {
            val type = message::class.java
            val realType = _messageEnc[type]?: return null
            val msg = MessageContainer()
            msg.identifier = realType
            msg.content = JsonParser().parse(_gson.toJson(message)).asJsonObject
            println(msg)
            return msg
        }


        private fun deserialize(container: MessageContainer?) : IMessage? {
            if (container == null)
                return null

            if (!_messageDec.containsKey(container.identifier))
                return null

            val typeClass = _messageDec[container.identifier]?: return null

            return _gson.fromJson<IMessage>(container.content, typeClass)
        }

        private fun onOpenFunc(isConnected: Boolean) {

            if (!isConnected)
                return

            val connectionRequest = ConnectionRequest()
            connectionRequest.login = _settingsManager.get_connection_settings().login
            connectionRequest.password = _settingsManager.get_connection_settings().password
            sendMessage(connectionRequest)
        }

    }
}