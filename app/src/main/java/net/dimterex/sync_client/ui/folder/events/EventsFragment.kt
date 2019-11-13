package net.dimterex.sync_client.ui.folder.sync

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_logs.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.EventDto
import net.dimterex.sync_client.presenter.menu.events.EventsPresenter
import net.dimterex.sync_client.presenter.menu.events.EventsView
import net.dimterex.sync_client.ui.MainActivity
import net.dimterex.sync_client.ui.base.BaseFragment
import net.dimterex.sync_client.ui.folder.sync.adapter.LogAdapter

class EventsFragment : BaseFragment<EventsPresenter>(), EventsView {
    override fun showMenu() {
        //main_bottom_navigation.visibility = View.VISIBLE
    }

    private lateinit var adapter: LogAdapter
    private lateinit var controller: NavController

    override fun update(logs: ArrayList<EventDto>) {
        adapter.update(logs)
    }

    override fun initPresenter(): EventsPresenter = EventsPresenter(this)

    override fun layoutId(): Int = R.layout.fragment_logs

    override fun initView() {
        controller =  Navigation.findNavController(activity as MainActivity, R.id.main_nav_host)
        adapter = LogAdapter(presenter::onRepoPressed)

        logs_list.layoutManager = LinearLayoutManager(logs_list.context)
        logs_list.adapter = adapter
    }

    override fun add_new_event(message: String){
        activity?.runOnUiThread {
            adapter.add(message)
            logs_list.scrollToPosition(adapter.itemCount - 1)
        }
    }
}
