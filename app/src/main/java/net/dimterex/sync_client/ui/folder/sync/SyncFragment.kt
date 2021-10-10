package net.dimterex.sync_client.ui.folder.sync

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sync_fragment_main.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.presenter.menu.sync.SyncPresenter
import net.dimterex.sync_client.presenter.menu.sync.SyncView
import net.dimterex.sync_client.ui.MainActivity
import net.dimterex.sync_client.ui.base.BaseFragment
import net.dimterex.sync_client.ui.folder.sync.adapter.SyncEventsAdapter
import net.dimterex.sync_client.ui.formatter.ConnectionIconFormatted

class SyncFragment : BaseFragment<SyncPresenter>(), SyncView {

    private lateinit var controller: NavController
    private lateinit var adapter: SyncEventsAdapter
    private var _connectionIconFormatted: ConnectionIconFormatted? = null

    override fun initPresenter(): SyncPresenter = SyncPresenter(this)

    override fun layoutId(): Int = R.layout.sync_fragment_main

    override fun initView() {
        controller =  Navigation.findNavController(activity as MainActivity, R.id.main_nav_host)

        adapter = SyncEventsAdapter(presenter::onRepoPressed, resources)
        _connectionIconFormatted = ConnectionIconFormatted(resources)

        logs_list.layoutManager = LinearLayoutManager(logs_list.context)
        logs_list.adapter = adapter

        sync_button.setOnClickListener {view ->
            presenter.sync_execute()
        }
    }

    override fun showMenu() {
        activity?.main_bottom_navigation?.visibility = View.VISIBLE
    }

    override fun update(logs: ArrayList<FileSyncState>) {
        adapter.update(logs)
    }

    override fun update_position(position: Int) {
        adapter.notifyItemChanged(position)
    }

    override fun update_connected(isConnected: Boolean) {
        connectionStatusTextView.text  =_connectionIconFormatted!!.format(isConnected)
    }

    override fun add_new_event(message: FileSyncState){
        activity?.runOnUiThread {
            adapter.add(message)
            logs_list.scrollToPosition(adapter.itemCount - 1)
        }
    }
}
