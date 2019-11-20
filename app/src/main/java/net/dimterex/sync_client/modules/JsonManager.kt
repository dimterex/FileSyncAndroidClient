package net.dimterex.sync_client.modules

import com.google.gson.Gson
import com.google.gson.JsonParser
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.MessageAttr
import net.dimterex.sync_client.api.Message.MessageContainer
import java.lang.reflect.Type
import java.util.HashMap
import kotlin.reflect.KFunction1
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlin.concurrent.thread


interface JsonManager {

    fun addListener(function: KFunction1<IMessage, Unit>)

    fun <T: Any> initApiMessage(classType: Class<T>)

    fun sendMessage(iMessage: IMessage)

    class Impl(private val _connection: ConnectionManager) : JsonManager {
        private val _gson : Gson = Gson()
        private val _messageEnc : HashMap<Type, String> = HashMap()
        private val _messageDec : HashMap<String, Type> = HashMap()

        private var _messageReceavedFunc: KFunction1<IMessage, Unit>? = null

        init {
            _connection.addListener(this::messageReceavedListener)
        }

        override fun addListener(function: KFunction1<IMessage, Unit>) {
            _messageReceavedFunc = function
        }

        private fun messageReceavedListener(raw_string: String)
        {
            var message = _gson.fromJson(raw_string, MessageContainer::class.java)
            var result = deserialize(message)
            if (result != null)
                _messageReceavedFunc?.invoke(result)
        }

        override fun <T: Any> initApiMessage(classType: Class<T>) {
            val messageAttr = classType.getAnnotation(MessageAttr::class.java) ?: return
            var id = messageAttr.name

            _messageDec[id]= classType
            _messageEnc[classType] = id
        }

        override fun sendMessage(iMessage: IMessage) {
            var msgArr = ArrayList<MessageContainer>()
            var msg: MessageContainer = serialize(iMessage) ?: return
            msgArr.add(msg)
            var result = _gson.toJson(msgArr)
            _connection.send(result)
        }

        private fun serialize(message: IMessage): MessageContainer? {
            val type = message::class.java
            var realType = _messageEnc[type]?: return null
            var msg = MessageContainer()
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

            var typeClass = _messageDec[container.identifier]?: return null

            return _gson.fromJson<IMessage>(container.content, typeClass)
        }

    }
}