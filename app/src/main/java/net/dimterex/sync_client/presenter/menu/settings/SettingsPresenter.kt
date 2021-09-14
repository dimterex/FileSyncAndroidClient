package net.dimterex.sync_client.presenter.menu.settings

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.dimterex.sync_client.data.entries.ConnectionsLocalModel
import net.dimterex.sync_client.modules.FileManager
import net.dimterex.sync_client.modules.SettingsManager
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView
import org.kodein.di.erased.instance
import java.io.File

class SettingsPresenter(private val view: SettingsView) : BasePresenter(view) {

    private val _settingsManager by instance<SettingsManager>()
    private val _fileManager by instance<FileManager>()



    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)

        view.profile = _settingsManager.get_connection_settings()
    }

    override fun onDestroy() {
        var new_sync_folder = view.get_sync_folder()

        _fileManager.set_sync_folder(new_sync_folder)

        _settingsManager.set_default_folder(new_sync_folder.absolutePath, String())

        _settingsManager.set_ip_address( view.get_ip_address())
        _settingsManager.set_ip_port( view.get_ip_port())

        _settingsManager.save_settings()

        super.onDestroy()
    }

//    fun onChoosePath(path: String, pathFile: File) {
//        _settingsManager.set_default_folder(path)
//        _fileManager.setDefaultDirectory(pathFile)
//    }
}

interface SettingsView : BaseView {

    var profile: ConnectionsLocalModel?

    fun get_ip_address(): String
    fun get_ip_port(): Int
    fun get_sync_folder(): File
}


