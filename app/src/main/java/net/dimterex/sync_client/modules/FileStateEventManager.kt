package net.dimterex.sync_client.modules

import net.dimterex.sync_client.entity.FileSyncState
import java.util.ArrayList
import kotlin.reflect.KFunction1

interface FileStateEventManager {

    val logs: ArrayList<FileSyncState>

    fun save_event(string: FileSyncState)
    fun add_event_listener(addEventFunc: KFunction1<FileSyncState, Unit>, updEventFunc: KFunction1<Int, Unit>)

    class Impl(private val _connection: ConnectionManager) : FileStateEventManager {

        private var _addEventFunc: KFunction1<FileSyncState, Unit>? = null
        private var _updEventFunc: KFunction1<Int, Unit>? = null

        override val logs: ArrayList<FileSyncState> = ArrayList<FileSyncState>()

        override fun add_event_listener(addEventFunc: KFunction1<FileSyncState, Unit>, updEventFunc: KFunction1<Int, Unit>) {
            _addEventFunc = addEventFunc
            _updEventFunc = updEventFunc
        }

        init {
            _connection.add_event_listener(this::save_event)
        }

        override fun save_event(fileSyncState: FileSyncState) {

            val threadId = Thread.currentThread().id
            println("THREAD save_event ${threadId}")

            logs.forEach { x ->
                if (x.id == fileSyncState.id) {
                    val index = logs.indexOf(x)
                    x.details = fileSyncState.details
                    _updEventFunc?.invoke(index)
                    return
                }
            }

            logs.add(fileSyncState)
            _addEventFunc?.invoke(fileSyncState)
        }
    }
}