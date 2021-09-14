package net.dimterex.sync_client.modules

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.dimterex.sync_client.data.database.RoomAppDatabase
import net.dimterex.sync_client.data.entries.ConnectionsLocalModel
import net.dimterex.sync_client.data.entries.FolderMappingLocalModel
import kotlin.concurrent.thread

import kotlin.reflect.KFunction0

interface SettingsManager {

    fun initialize()
    fun get_connection_settings() : ConnectionsLocalModel
    fun get_folder_mapping() : List<FolderMappingLocalModel>

    fun save_settings()
    fun set_default_folder(inside_folder: String, outside_folder: String)
    fun set_ip_address(getIpAddress: String)
    fun set_ip_port(getIpPort: Int)

    fun add_listener(restart: KFunction0<Unit>)

    class Impl(private val _repoDao: RoomAppDatabase) : SettingsManager {

        private var _settingsReadedAction: ArrayList<KFunction0<Unit>> = ArrayList()
        private var _isNeedToReconnect = false

        private var _connectionSettings: ConnectionsLocalModel? = null
        private var _folderMapping: List<FolderMappingLocalModel>? = null

        private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + Job())


        override fun add_listener(restart: KFunction0<Unit>) {
            _settingsReadedAction.add(restart)
        }

        override fun initialize() {

            scope.launch {
                var connection_model = ConnectionsLocalModel(0, "192.168.0.235", 1234, "mobile", "mobile")
                _repoDao.connectionSettingsDao().insert(connection_model)

                var folder_mappong = FolderMappingLocalModel("/storage/emulated/0/Download", "D:\\SyncTest")

                _repoDao.folderMappingDao().insert(folder_mappong)

                _connectionSettings = _repoDao.connectionSettingsDao().selectById(0)
                _folderMapping = _repoDao.folderMappingDao().getAll()
                _settingsReadedAction.forEach { x ->
                    x.invoke()
                }
            }
        }

        override fun get_connection_settings() : ConnectionsLocalModel {
            return _connectionSettings!!
        }

        override fun get_folder_mapping(): List<FolderMappingLocalModel> {
            return _folderMapping!!
        }

        override fun save_settings() {
            scope.launch {
                _repoDao.connectionSettingsDao().update(_connectionSettings!!)

                _folderMapping!!.forEach { x ->
                    _repoDao.folderMappingDao().update(x)
                }

                if (_isNeedToReconnect) {
                    thread (true) {
                        _settingsReadedAction.forEach { x ->
                            x.invoke()
                        }
                    }
                }
            }
        }

        override fun set_default_folder(inside_folder: String, outside_folder: String ) {
            _folderMapping!!.plus(FolderMappingLocalModel(inside_folder, outside_folder))
        }

        override fun set_ip_address(new_ip_address: String) {
            _isNeedToReconnect = _connectionSettings!!.ip_address != new_ip_address
            _connectionSettings!!.ip_address = new_ip_address
        }

        override fun set_ip_port(new_ip_port: Int) {
            _isNeedToReconnect = _connectionSettings!!.ip_port != new_ip_port
            _connectionSettings!!.ip_port = new_ip_port
        }
    }
}
