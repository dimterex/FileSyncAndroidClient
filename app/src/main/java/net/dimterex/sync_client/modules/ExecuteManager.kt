package net.dimterex.sync_client.modules

import kotlinx.coroutines.launch
import net.dimterex.sync_client.api.interfaces.IMessage
import net.dimterex.sync_client.api.Modules.Common.IExecute
import net.dimterex.sync_client.data.ScopeFactory
import java.lang.reflect.Type
import java.util.HashMap
import kotlin.concurrent.thread

interface ExecuteManager {

    fun sendMessage(param: IMessage)

    fun initApiMessage(classType: Class<*>, kFunction2: IExecute<IMessage>)

    fun execute(iMessage: IMessage)

    class Impl(private val _jsonManager : JsonManager,
               private val _scopeFactory: ScopeFactory
    ) : ExecuteManager {

        private val _typesAction: HashMap<Type, IExecute<IMessage>> = HashMap()
        private val _scope = _scopeFactory.getScope()

        init {
            _jsonManager.addListener(this::execute)
        }

        override fun initApiMessage(classType: Class<*>, kFunction2: IExecute<IMessage>) {
            _typesAction[classType] = kFunction2
            _jsonManager.initApiMessage(classType)
        }

        override fun execute(iMessage: IMessage) {
            _scope.launch {
                if (!_typesAction.containsKey(iMessage.javaClass))
                    return@launch

                val executeMethod: IExecute<IMessage> = _typesAction[iMessage.javaClass] ?: return@launch
                executeMethod.Execute(iMessage)
            }
        }

        override fun sendMessage(param: IMessage) {
            _scope.launch {
                _jsonManager.sendMessage(param)
            }
        }
    }
}