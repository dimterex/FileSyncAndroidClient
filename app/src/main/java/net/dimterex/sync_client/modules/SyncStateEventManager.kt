package net.dimterex.sync_client.modules

import kotlinx.coroutines.launch
import net.dimterex.sync_client.data.ScopeFactory
import kotlin.reflect.KFunction1

interface SyncStateEventManager {
    fun save_event(string: String)
    fun add_event_listener(updEventFunc: KFunction1<String, Unit>)

    class Impl(private val _scopeFactory: ScopeFactory) : SyncStateEventManager {

        private val _scope = _scopeFactory.getMainScope()

        private var _updEventFunc: KFunction1<String, Unit>? = null

        override fun save_event(string: String) {
            _scope.launch {
                _updEventFunc?.invoke(string)
            }
        }

        override fun add_event_listener(updEventFunc: KFunction1<String, Unit>) {
            _updEventFunc = updEventFunc
        }
    }
}