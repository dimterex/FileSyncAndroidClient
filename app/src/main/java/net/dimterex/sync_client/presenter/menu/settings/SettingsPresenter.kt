package net.dimterex.sync_client.presenter.menu.settings

import android.os.Bundle
import net.dimterex.sync_client.data.entries.ConnectionsLocalModel
import net.dimterex.sync_client.data.entries.FolderMappingLocalModel
import net.dimterex.sync_client.entity.FolderSelectModel
import net.dimterex.sync_client.modules.AvailableFoldersManager
import net.dimterex.sync_client.modules.ConnectionManager
import net.dimterex.sync_client.modules.SettingsManager
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView
import org.kodein.di.erased.instance

class SettingsPresenter(private val view: SettingsView) : BasePresenter(view) {

    private val _settingsManager by instance<SettingsManager>()
    private val _connectionManager by instance<ConnectionManager>()
    private val _availableFoldersManager  by instance<AvailableFoldersManager>()

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        view.profile = _settingsManager.get_connection_settings()
        _availableFoldersManager.subscribe_added_event(this::add_event_listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        _availableFoldersManager.unsubscribe_added_event()
    }

    private fun add_event_listener(message: String){
        view.add_new_event(message)
    }

    fun getMappingFolders(): List<FolderMappingLocalModel> {
        return _settingsManager.get_folder_mapping();
    }

    fun getAvailableFolders(): List<String> {
        return _availableFoldersManager.logs
    }

    fun save() {
        val new_sync_folder = view.get_sync_folders()

        val connectionSettings = _settingsManager.get_connection_settings()
        connectionSettings.ip_address = view.get_ip_address()
        connectionSettings.ip_port = view.get_ip_port()
        connectionSettings.login = view.get_login()
        connectionSettings.password = view.get_password()

        _settingsManager.update_settings(new_sync_folder.map { it.folFolderMappingLocalModel })
    }


    fun check_connection(url: String, port: Int, login: String, password: String) {
        val settings = _settingsManager.get_connection_settings()
        settings.ip_address = url
        settings.ip_port = port
        settings.login = login
        settings.password = password

        _connectionManager.restart_connection()
    }
}

interface SettingsView : BaseView {

    var profile: ConnectionsLocalModel?

    fun add_new_event(message: String)
    fun get_ip_address(): String
    fun get_ip_port(): Int
    fun get_login(): String
    fun get_password(): String
    fun get_sync_folders(): Array<FolderSelectModel>
}


