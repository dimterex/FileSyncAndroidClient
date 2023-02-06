package net.dimterex.sync_client.modules

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.MessageAttr
import net.dimterex.sync_client.api.Message.MessageContainer
import java.lang.reflect.Type
import java.util.HashMap
import kotlin.reflect.KFunction1
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response


interface JsonManager {

    fun addListener(function: KFunction1<IMessage, Unit>)

    fun <T: Any> initApiMessage(classType: Class<T>)

    fun restResponse(inputStream: String, type: Type)

    suspend fun getPostMessage(iMessage: IMessage): Response<ResponseBody>

    class Impl(private val _connection: ConnectionManager) : JsonManager {
        private val _gson : Gson = Gson()
        private val _messageEnc : HashMap<Type, String> = HashMap()
        private val _messageDec : HashMap<String, Type> = HashMap()
        private val TAG = this::class.java.name

        private var _messageReceivedFunc: KFunction1<IMessage, Unit>? = null

        override fun addListener(function: KFunction1<IMessage, Unit>) {
            _messageReceivedFunc = function
        }

        private fun messageReceivedListener(raw_string: String)
        {
            Log.d(TAG, "${raw_string}")
            val messages = _gson.fromJson(raw_string, Array<MessageContainer>::class.java)
            for (message in messages)
            {
                val result = deserialize(message)
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

        override fun restResponse(inputStream: String, type: Type) {
            messageReceivedListener(inputStream)
        }

        override suspend fun getPostMessage(iMessage: IMessage): Response<ResponseBody> {
            return _connection.send_request(createJsonRequestBody(iMessage))
        }

        private fun createJsonRequestBody(iMessage: IMessage) : RequestBody
        {
            val msgArr = ArrayList<MessageContainer>()
            val msg: MessageContainer = serialize(iMessage)!!
            msgArr.add(msg)
            val result = _gson.toJson(msgArr)

            return RequestBody.create(
                    okhttp3.MediaType.parse("application/json; charset=utf-8"),
                    result)
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

    }
}