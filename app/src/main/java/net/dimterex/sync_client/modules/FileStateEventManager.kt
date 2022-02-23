package net.dimterex.sync_client.modules

import kotlinx.coroutines.launch
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.entity.FileSyncState
import java.util.ArrayList
import kotlin.reflect.KFunction1

interface FileStateEventManager {

    val logs: ArrayList<FileSyncState>

    fun save_event(string: FileSyncState)

    fun clear_log()
    fun subscribe_added_event(addEventFunc: KFunction1<FileSyncState, Unit>)
    fun subscribe_updated_event(updEventFunc: KFunction1<Int, Unit>)
    fun subscribe_clear_event(clear_event:() -> Unit)
    fun unsubscribe_added_event()
    fun unsubscribe_updated_event()
    fun unsubscribe_clear_event()

    class Impl(private val _scopeFactory: ScopeFactory) : FileStateEventManager {

        private var _addEventFunc: KFunction1<FileSyncState, Unit>? = null
        private var _updEventFunc: KFunction1<Int, Unit>? = null
        private var _clear_event: (() -> Unit?)? = null
        private val _scope = _scopeFactory.getMainScope()

        override val logs: ArrayList<FileSyncState> = ArrayList<FileSyncState>()

        override fun clear_log() {
            _clear_event?.invoke()
        }

        override fun subscribe_added_event(addEventFunc: KFunction1<FileSyncState, Unit>) {
            _addEventFunc = addEventFunc
        }

        override fun subscribe_updated_event(updEventFunc: KFunction1<Int, Unit>) {
            _updEventFunc = updEventFunc
        }

        override fun subscribe_clear_event(clear_event: () -> Unit) {
            _clear_event = clear_event
        }

        override fun unsubscribe_added_event() {
            _addEventFunc = null
        }

        override fun unsubscribe_updated_event() {
            _updEventFunc = null
        }

        override fun unsubscribe_clear_event() {
            _clear_event = null
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