package net.dimterex.sync_client.ui.folder.sync

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import net.dimterex.sync_client.R
import kotlinx.android.synthetic.main.sync_fragment_main.*
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.entity.FileSyncType
import net.dimterex.sync_client.presenter.menu.sync.SyncPresenter
import net.dimterex.sync_client.presenter.menu.sync.SyncView
import net.dimterex.sync_client.ui.base.BaseFragment
import net.dimterex.sync_client.ui.folder.sync.adapter.SyncEventsAdapter
import net.dimterex.sync_client.ui.formatter.ConnectionIconFormatted


class SyncFragment : BaseFragment<SyncPresenter>(), SyncView {

    private var _menu: Menu? = null
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
    }

    override fun update(logs: ArrayList<FileSyncState>) {
        adapter.update(logs)
    }

    override fun update_position(position: Int) {
        adapter.notifyItemChanged(position)
        logs_list.scrollToPosition(position)
    }

    override fun update_connected(isConnected: Boolean) {
        if (_menu == null)
            return

        val statusItem = _menu!!.findItem(R.id.connectionStatusTextView) ?: return
        statusItem.title =_connectionIconFormatted!!.format(isConnected)
    }

    override fun updateSyncState(state: String) {
        if (_menu == null)
            return

        val statusItem = _menu!!.findItem(R.id.syncStatusTextView) ?: return
        statusItem.title = state
    }

    override fun add_new_event(message: FileSyncState){
        adapter.add(message)
    }

    override fun clear_events() {
        adapter.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.sync_fragment_menu, menu)
        _menu = menu;
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sync_button -> {

                updateSyncState(String())
                if (presenter.can_sync_execute())
                    SyncDialogFragment().show(this.childFragmentManager, "SyncDialogFragment")
                true
            }
            R.id.connectionStatusTextView -> {
                add_new_event(FileSyncState("SOME PATh", "outside_path", FileSyncType.UPDATE, "1/4444", 50))
                true
            }

            else -> false
        }
    }
}
