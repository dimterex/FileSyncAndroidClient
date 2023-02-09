package net.dimterex.sync_client.modules

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.dimterex.sync_client.data.ScopeFactory
import net.dimterex.sync_client.data.database.RoomAppDatabase
import net.dimterex.sync_client.data.entries.ConnectionsLocalModel
import net.dimterex.sync_client.data.entries.FolderMappingLocalModel

import kotlin.reflect.KFunction0

interface SettingsManager {

    fun initialize()
    fun get_connection_settings() : ConnectionsLocalModel
    fun get_folder_mapping() : List<FolderMappingLocalModel>
    fun update_settings(new_folders: List<FolderMappingLocalModel>)
    fun add_listener(restart: KFunction0<Unit>)


    class Impl(private val _repoDao: RoomAppDatabase,
               private val _scopeFactory: ScopeFactory
    ) : SettingsManager {

        private val TAG = this::class.java.name

        private var _settingsReadedAction: ArrayList<KFunction0<Unit>> = ArrayList()

        private var _connectionSettings: ConnectionsLocalModel? = null
        private var _folderMapping: List<FolderMappingLocalModel>? = null

        private val scope: CoroutineScope = _scopeFactory.getScope()


        override fun add_listener(restart: KFunction0<Unit>) {
            _settingsReadedAction.add(restart)
        }

        override fun initialize() {
            val job = scope.launch {
                _connectionSettings = _repoDao.connectionSettingsDao().selectById(DEFAULT_CONNECTION_ID)

                if (_connectionSettings == null)
                    _connectionSettings = ConnectionsLocalModel(DEFAULT_CONNECTION_ID, String(), DEFAULT_PORT, String(), String())

                _folderMapping = _repoDao.folderMappingDao().getAll()

                _folderMapping!!.forEach {
                    Log.d(TAG, "Loaded ${it.id}: ${it.inside_folder}")
                }
            }

            job.invokeOnCompletion {
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

        override fun update_settings(new_folders: List<FolderMappingLocalModel>) {
            scope.launch {

                _repoDao.folderMappingDao().deleteAll()

                _repoDao.connectionSettingsDao().insert(_connectionSettings!!)

                new_folders.forEach {
                    _repoDao.folderMappingDao().insert(it)
                    Log.d(TAG, "Saved ${it.id}: ${it.inside_folder}")
                }

                initialize()

                Log.d(TAG, "Reinitialize" )
            }
        }

        companion object {
            const val  DEFAULT_CONNECTION_ID = 0;
            const val  DEFAULT_PORT = 8080;
        }
    }
}
