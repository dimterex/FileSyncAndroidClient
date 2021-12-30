package net.dimterex.sync_client.modules

import kotlinx.coroutines.launch
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.entity.FileSyncState
import java.util.ArrayList
import kotlin.reflect.KFunction1

interface FileStateEventManager {

    val logs: ArrayList<FileSyncState>

    fun save_event(string: FileSyncState)
    fun add_event_listener(
        addEventFunc: KFunction1<FileSyncState, Unit>,
        updEventFunc: KFunction1<Int, Unit>,
        clear_event: () -> Unit
    )
    fun clear_log()

    class Impl(private val _scopeFactory: ScopeFactory) : FileStateEventManager {

        private var _addEventFunc: KFunction1<FileSyncState, Unit>? = null
        private var _updEventFunc: KFunction1<Int, Unit>? = null
        private var _clear_event: (() -> Unit?)? = null
        private val _scope = _scopeFactory.getMainScope()

        override val logs: ArrayList<FileSyncState> = ArrayList<FileSyncState>()

        override fun add_event_listener(addEventFunc: KFunction1<FileSyncState, Unit>, updEventFunc: KFunction1<Int, Unit>,  clear_event: () -> Unit) {
            _addEventFunc = addEventFunc
            _updEventFunc = updEventFunc
            _clear_event = clear_event
        }

        override fun clear_log() {
            _clear_event?.invoke()
        }

        override fun save_event(fileSyncState: FileSyncState) {

            for (log in logs)
            {
                if (log.inside_path == fileSyncState.inside_path) {
                    val index = logs.indexOf(log)
                    log.process = fileSyncState.process
                    _scope.launch {
                        _updEventFunc?.invoke(index)
                    }
                    return
                }
            }
            logs.add(fileSyncState)
            _scope.launch {
                _addEventFunc?.invoke(fileSyncState)
            }
        }
    }
}