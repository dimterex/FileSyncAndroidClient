package net.dimterex.sync_client.modules

import net.dimterex.sync_client.entity.EventDto
import java.util.ArrayList
import kotlin.reflect.KFunction1

interface EventLoggerManager {

    val logs: ArrayList<EventDto>

    fun save_event(string: EventDto)
    fun add_event_listener(addEventFunc: KFunction1<EventDto, Unit>, updEventFunc: KFunction1<Int, Unit>)

    class Impl(private val _connection: ConnectionManager) : EventLoggerManager {

        private var _addEventFunc: KFunction1<EventDto, Unit>? = null
        private var _updEventFunc: KFunction1<Int, Unit>? = null

        override val logs: ArrayList<EventDto> = ArrayList<EventDto>()

        override fun add_event_listener(addEventFunc: KFunction1<EventDto, Unit>, updEventFunc: KFunction1<Int, Unit>) {
            _addEventFunc = addEventFunc
            _updEventFunc = updEventFunc
        }

        init {
            _connection.add_event_listener(this::save_event)
        }

        override fun save_event(eventDto: EventDto) {

            val threadId = Thread.currentThread().id
            println("THREAD save_event ${threadId}")

            logs.forEach { x ->
                if (x.id == eventDto.id) {
                    val index = logs.indexOf(x)
                    x.details = eventDto.details
                    _updEventFunc?.invoke(index)
                    return
                }
            }

            logs.add(eventDto)
            _addEventFunc?.invoke(eventDto)
        }
    }
}