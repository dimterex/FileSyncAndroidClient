package net.dimterex.sync_client.ui.folder.sync

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.sync_fragment_main.*
import net.dimterex.sync_client.BuildConfig
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.presenter.menu.sync.SyncPresenter
import net.dimterex.sync_client.presenter.menu.sync.SyncView
import net.dimterex.sync_client.ui.base.BaseFragment
import net.dimterex.sync_client.ui.folder.sync.adapter.SyncEventsAdapter
import net.dimterex.sync_client.ui.formatter.AutoscrollButtonFormatter
import net.dimterex.sync_client.ui.formatter.ConnectionIconFormatted


class SyncFragment : BaseFragment<SyncPresenter>(), SyncView {

    private var _menu: Menu? = null
    private val TAG = this::class.java.name
    private lateinit var controller: NavController
    private lateinit var adapter: SyncEventsAdapter
    private var _connectionIconFormatted: ConnectionIconFormatted? = null
    private var _isAutoscrollToPositionEnabled: Boolean = true
    private val _autoscrollButtonFormatter = AutoscrollButtonFormatter()

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

        if (_isAutoscrollToPositionEnabled)
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

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.sync_fragment_menu, menu)
        _menu = menu;

        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }

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
                if (BuildConfig.DEBUG) {
                    presenter.test_action()
                }
                true
            }

            R.id.syncStatusTextView -> {
                presenter.executeLast()
                return true
            }

            R.id.autoscroll_button -> {
                val autoscroll_button = _menu!!.findItem(R.id.autoscroll_button) ?: return true
                _isAutoscrollToPositionEnabled = !_isAutoscrollToPositionEnabled
                autoscroll_button.icon = ContextCompat.getDrawable(context!!, _autoscrollButtonFormatter.format(_isAutoscrollToPositionEnabled));

                return true
            }
            else -> false
        }
    }
}
