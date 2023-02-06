package net.dimterex.sync_client.modules

import android.util.Log
import net.dimterex.sync_client.api.Message.Sync.SyncStateFilesResponse
import kotlin.reflect.KFunction6
import kotlin.reflect.KFunction1

interface SyncStateManager {

    var syncStateFilesResponse: SyncStateFilesResponse?

    fun changeState(isSyncEnabled: Boolean)

    fun subscribeStateChange(event: KFunction1<Boolean, Unit>)
    fun unsubscribeStateChange(event: KFunction1<Boolean, Unit>)

    fun subscribe_update(kFunction0: KFunction6<List<String>, List<String>, List<String>, List<String>, List<String>, List<String>, Unit>)
    fun unsubscribe_update()


    class Impl() : SyncStateManager {
        private var _update_func: KFunction6<List<String>, List<String>, List<String>, List<String>, List<String>, List<String>, Unit>? = null
        private val _stateChanges: ArrayList<KFunction1<Boolean, Unit>> = ArrayList()
        private val TAG = this::class.java.name

        override var syncStateFilesResponse: SyncStateFilesResponse? = null
            set(value) {
                raise_update(value)

                field = value
            }

        override fun changeState(isSyncEnabled: Boolean) {
            _stateChanges.forEach {
                it.invoke(isSyncEnabled)
            }
        }

        override fun subscribeStateChange(event: KFunction1<Boolean, Unit>) {
            _stateChanges.add(event)
        }

        override fun unsubscribeStateChange(event: KFunction1<Boolean, Unit>) {
            if (_stateChanges.contains(event))
                _stateChanges.remove(event)
        }

        override fun subscribe_update(update_func: KFunction6<List<String>, List<String>, List<String>, List<String>, List<String>, List<String>, Unit>) {
            _update_func = update_func
            Log.d(TAG, "subscribe_update: ${syncStateFilesResponse == null}")
            raise_update(syncStateFilesResponse)
        }

        override fun unsubscribe_update() {
            _update_func = null
        }

        private fun raise_update(value: SyncStateFilesResponse?) {
            if (value == null) {
                return
            }

            if (_update_func == null) {
                return
            }

            _update_func!!.invoke(
                value.added_files.map { x -> x.file_name.last() },
                value.removed_files.map { x -> x.file_name.last() },
                value.uploaded_files.map { x -> x.file_name.last() },
                value.updated_files.map { x -> x.file_name.last() },
                value.server_removed_files.map { x -> x.file_name.last() },
                value.database_added_files.map { x -> x.file_name.last() })
        }
    }
}