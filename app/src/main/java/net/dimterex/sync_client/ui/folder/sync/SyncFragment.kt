package net.dimterex.sync_client.ui.folder.sync

import android.util.Log
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

    private val TAG = this::class.java.name
    private lateinit var controller: NavController
    private lateinit var adapter: SyncEventsAdapter
    private var _connectionIconFormatted: ConnectionIconFormatted? = null

    override fun initPresenter(): SyncPresenter = SyncPresenter(this)

    override fun layoutId(): Int = R.layout.sync_fragment_main

    override fun initView() {
        controller =  Navigation.findNavController(activity as MainActivity, R.id.main_nav_host)

        adapter = SyncEventsAdapter(presenter::onRepoPressed, resources)
        _connectionIconFormatted = ConnectionIconFormatted(resources)

        logs_list.layoutManager = LinearLayoutManager(logs_list.context, LinearLayoutManager.VERTICAL, false)
        logs_list.adapter = adapter

        sync_button.setOnClickListener {view ->
            presenter.sync_execute()
        }
        menu_button.setOnClickListener { view ->
//            main_container?.openDrawer(Gravity.START)
        }
    }

    override fun update(logs: ArrayList<FileSyncState>) {
        adapter.update(logs)
        Log.d(TAG, "Old events update: $logs")
    }

    override fun update_position(position: Int) {
        activity?.runOnUiThread {
            adapter.notifyItemChanged(position)
            logs_list.scrollToPosition(position)
        }

        Log.d(TAG, "Old event update by position: $position")
    }

    override fun update_connected(isConnected: Boolean) {
        connectionStatusTextView.text  =_connectionIconFormatted!!.format(isConnected)
    }

    override fun add_new_event(message: FileSyncState){
        activity?.runOnUiThread {
            adapter.add(message)
        }

        Log.d(TAG, "New event added: $message")
    }
}
