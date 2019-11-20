package net.dimterex.sync_client.modules

import net.dimterex.sync_client.entity.EventDto
import java.util.ArrayList
import kotlin.reflect.KFunction1

interface EventLoggerManager {

    val logs: ArrayList<EventDto>

    fun save_event(string: String)
    fun add_event_listener(function: KFunction1<String, Unit>)

    class Impl(private val _connection: ConnectionManager) : EventLoggerManager {

        private var _event_listener: KFunction1<String, Unit>? = null

        override val logs: ArrayList<EventDto> = ArrayList<EventDto>()

        override fun add_event_listener(function: KFunction1<String, Unit>) {
            _event_listener = function
        }

        init {
            _connection.add_event_listener(this::save_event)
        }

        override fun save_event(string: String) {

            val threadId = Thread.currentThread().id
            println("THREAD save_event ${threadId}")

            logs.add(EventDto(0, string, string, string))
            _event_listener?.invoke(string)
        }
    }
}