package net.dimterex.sync_client.modules

import net.dimterex.sync_client.api.Message.Sync.SyncStateFilesResponse
import kotlin.reflect.KFunction6

interface SyncStateManager {

    var syncStateFilesResponse: SyncStateFilesResponse?

    fun subscribe_update(kFunction0: KFunction6<List<String>, List<String>, List<String>, List<String>, List<String>, List<String>, Unit>)
    fun unsubscribe_update()


    class Impl() : SyncStateManager{
        private var _update_func: KFunction6<List<String>, List<String>, List<String>, List<String>, List<String>, List<String>, Unit>? = null

        override var syncStateFilesResponse: SyncStateFilesResponse? = null
            set(value) {

                if (value != null) {
                    _update_func?.invoke(value.added_files.map { x -> x.file_name.last() },
                        value.removed_files.map { x -> x.file_name.last() },
                        value.uploaded_files.map { x -> x.file_name.last() },
                        value.updated_files.map { x -> x.file_name.last() },
                        value.server_removed_files.map { x -> x.file_name.last() },
                        value.database_added_files.map { x -> x.file_name.last() })
                }

                field = value
            }


        override fun subscribe_update(update_func: KFunction6<List<String>, List<String>, List<String>, List<String>, List<String>, List<String>, Unit>) {
            _update_func = update_func
        }

        override fun unsubscribe_update() {
            _update_func = null
        }
    }
}