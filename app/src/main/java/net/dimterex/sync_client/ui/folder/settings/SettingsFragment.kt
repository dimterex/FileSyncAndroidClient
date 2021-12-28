package net.dimterex.sync_client.ui.folder.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.settings_fragment_main.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.data.entries.ConnectionsLocalModel
import net.dimterex.sync_client.data.entries.FolderMappingLocalModel
import net.dimterex.sync_client.entity.FolderSelectModel
import net.dimterex.sync_client.presenter.menu.settings.SettingsPresenter
import net.dimterex.sync_client.presenter.menu.settings.SettingsView
import net.dimterex.sync_client.ui.base.BaseFragment
import net.dimterex.sync_client.ui.folder.sync.adapter.FolderSelectionAdapter

class SettingsFragment : BaseFragment<SettingsPresenter>(), SettingsView {

    private var _menu: Menu? = null
    private lateinit var adapter: FolderSelectionAdapter
    private val _outside_folders = ArrayList<String>()

    override var profile: ConnectionsLocalModel? = null
    set(value) {

        if (value != null) {
            ip_address_textbox?.editText?.setText(value.ip_address)
            port_textbox?.editText?.setText(value.ip_port.toString())
            login_textbox?.editText?.setText(value.login)
            password_textbox?.editText?.setText(value.password)
        }

        field = value
    }

    override fun add_new_event(message: String) {
        _outside_folders.add(message)
        adapter.items.forEach { x ->
            x.folders.clear()
            x.folders.addAll(_outside_folders)
//            adapter.notifyItemChanged(adapter.items.indexOf(x))
        }
    }

    override fun initPresenter(): SettingsPresenter = SettingsPresenter(this)

    override fun layoutId(): Int = R.layout.settings_fragment_main

    override fun initView() {

        presenter.getAvailableFolders().forEach { x ->
            _outside_folders.add(x)
        }

        adapter = FolderSelectionAdapter()
        folders_list.layoutManager = LinearLayoutManager(folders_list.context)
        folders_list.adapter = adapter

        presenter.getMappingFolders().forEach{ x ->
            adapter.add(FolderSelectModel(x, _outside_folders))
        }
    }

    override fun get_ip_port(): Int {
        return port_textbox?.editText?.text.toString().toInt()
    }

    override fun get_login(): String {
        return login_textbox?.editText?.text.toString()
    }

    override fun get_password(): String {
        return password_textbox?.editText?.text.toString()
    }

    override fun get_sync_folders(): Array<FolderSelectModel> {
        return adapter.items.toTypedArray()
    }

    override fun get_ip_address(): String  {
        return ip_address_textbox?.editText?.text.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.settings_fragment_menu, menu)
        _menu = menu;
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saveSettingsButton -> {
                presenter.save()
                true
            }
            R.id.addFolderButton -> {
                adapter.add(FolderSelectModel(createFolderMappingLocalModel(), _outside_folders))
                true
            }
            R.id.checkConnectionButton -> {
                presenter.check_connection(get_ip_address(), get_ip_port(), get_login(), get_password())
                true
            }

            else -> false
        }
    }

    private fun createFolderMappingLocalModel(): FolderMappingLocalModel {
        var i = 0

        for (item in adapter.items)
        {
            if (i == item.folFolderMappingLocalModel.id)
            {
                i++
            }
            else
            {
                break
            }
        }

        return FolderMappingLocalModel(i, String(), String())
    }
}
