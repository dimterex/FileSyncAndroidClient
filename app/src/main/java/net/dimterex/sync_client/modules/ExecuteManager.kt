package net.dimterex.sync_client.modules

import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.Modules.Common.IExecute
import java.lang.reflect.Type
import java.util.HashMap
import kotlin.concurrent.thread

interface ExecuteManager {

    fun sendMessage(param: IMessage)

    fun initApiMessage(classType: Class<*>, kFunction2: IExecute<IMessage>)

    fun execute(iMessage: IMessage)

    class Impl(private val _jsonManager : JsonManager) : ExecuteManager {

        private val _typesAction: HashMap<Type, IExecute<IMessage>> = HashMap()

        init {
            _jsonManager.addListener(this::execute)
        }

        override fun initApiMessage(classType: Class<*>, kFunction2: IExecute<IMessage>) {
            _typesAction[classType] = kFunction2
            _jsonManager.initApiMessage(classType)
        }

        override fun execute(iMessage: IMessage) {
            if (!_typesAction.containsKey(iMessage.javaClass))
                return

            val threadId = Thread.currentThread().id
            println("THREAD execute manager ${threadId}")

            var executeMethod: IExecute<IMessage> = _typesAction[iMessage.javaClass] ?: return

            executeMethod.Execute(iMessage)
        }

        override fun sendMessage(param: IMessage) {
            thread (start = true) {
                _jsonManager.sendMessage(param)
                val threadId = Thread.currentThread().id
                println("THREAD execute manager sended ${threadId}")
            }
        }
    }
}