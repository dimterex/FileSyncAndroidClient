package net.dimterex.sync_client.presenter.menu.events

import android.os.Bundle
import net.dimterex.sync_client.entity.EventDto
import net.dimterex.sync_client.modules.EventLoggerManager
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView
import org.kodein.di.erased.instance

class EventsPresenter(private val view: EventsView) : BasePresenter(view) {

    private val _event_log_manager by instance<EventLoggerManager>()

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)

        view.update(_event_log_manager.logs)
        _event_log_manager.add_event_listener(this::add_event_listener)
    }

    private fun add_event_listener(message: String){
        view.add_new_event(message)
    }

//
    fun onRepoPressed(id: Long) {
//        view.navigateRepoDetails(id)
    }
//
//    fun add_event_listener(string: String) {
//        view.event_change(string)
//    }

}

interface EventsView : BaseView {

    fun add_new_event(message: String)
    fun update(logs: ArrayList<EventDto>)
}