package net.dimterex.sync_client.ui.folder.sync

import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.sync_fragment_main.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.presenter.menu.sync.SyncPresenter
import net.dimterex.sync_client.presenter.menu.sync.SyncView
import net.dimterex.sync_client.ui.base.BaseFragment
import net.dimterex.sync_client.ui.folder.sync.adapter.SyncEventsAdapter
import net.dimterex.sync_client.ui.formatter.ConnectionIconFormatted

class SyncFragment : BaseFragment<SyncPresenter>(), SyncView {

    private val TAG = this::class.java.name
    private lateinit var controller: NavController //не обязательно хранить
    private lateinit var adapter: SyncEventsAdapter
    private var _connectionIconFormatted: ConnectionIconFormatted? = null

    override fun initPresenter(): SyncPresenter = SyncPresenter(this)

    override fun layoutId(): Int = R.layout.sync_fragment_main

    override fun initView() {
        controller = findNavController()

        adapter = SyncEventsAdapter(presenter::onRepoPressed, resources)
        _connectionIconFormatted = ConnectionIconFormatted(resources)

        logs_list.layoutManager = LinearLayoutManager(this.context)
        logs_list.adapter = adapter

        sync_button.setOnClickListener {view ->
            presenter.sync_execute()
        }
    }

    override fun update(logs: ArrayList<FileSyncState>) {
        adapter.update(logs)
    }

    override fun update_position(position: Int) {
        adapter.notifyItemChanged(position)
        logs_list.scrollToPosition(position)
    }

    override fun update_connected(isConnected: Boolean) {
        connectionStatusTextView.text  =_connectionIconFormatted!!.format(isConnected)
    }

    override fun add_new_event(message: FileSyncState){
        adapter.add(message)
    }
}
