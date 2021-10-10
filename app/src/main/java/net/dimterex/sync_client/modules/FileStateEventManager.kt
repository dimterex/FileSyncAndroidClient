package net.dimterex.sync_client.modules

import kotlinx.coroutines.launch
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.entity.FileSyncState
import java.util.ArrayList
import kotlin.reflect.KFunction1

interface FileStateEventManager {

    val logs: ArrayList<FileSyncState>

    fun save_event(string: FileSyncState)
    fun add_event_listener(addEventFunc: KFunction1<FileSyncState, Unit>, updEventFunc: KFunction1<Int, Unit>)

    class Impl(private val _scopeFactory: ScopeFactory) : FileStateEventManager {

        private var _addEventFunc: KFunction1<FileSyncState, Unit>? = null
        private var _updEventFunc: KFunction1<Int, Unit>? = null

        private val _scope = _scopeFactory.getMainScope()

        override val logs: ArrayList<FileSyncState> = ArrayList<FileSyncState>()

        override fun add_event_listener(addEventFunc: KFunction1<FileSyncState, Unit>, updEventFunc: KFunction1<Int, Unit>) {
            _addEventFunc = addEventFunc
            _updEventFunc = updEventFunc
        }

        override fun save_event(fileSyncState: FileSyncState) {
            _scope.launch {
                logs.forEach { x ->
                    if (x.id == fileSyncState.id) {
                        val index = logs.indexOf(x)
                        x.details = fileSyncState.details
                        _updEventFunc?.invoke(index)
                        return@launch
                    }
                }

                logs.add(fileSyncState)
                _addEventFunc?.invoke(fileSyncState)
            }
        }
    }
}