package net.dimterex.sync_client.modules

import net.dimterex.sync_client.api.Message.Sync.SyncStateFilesResponse
import kotlin.reflect.KFunction6

interface SyncStateManager {

    var syncStateFilesResponse: SyncStateFilesResponse?

    fun subscribe_update(kFunction0: KFunction6<Int, Int, Int, Int, Int, Int, Unit>)
    fun unsubscribe_update()


    class Impl() : SyncStateManager{
        private var _update_func: KFunction6<Int, Int, Int, Int, Int, Int, Unit>? = null

        override var syncStateFilesResponse: SyncStateFilesResponse? = null
            set(value) {

                if (value != null) {
                    _update_func?.invoke(value.added_files.count(),
                        value.removed_files.count(),
                        value.uploaded_files.count(),
                        value.updated_files.count(),
                        value.server_removed_files.count(),
                        value.database_added_files.count())
                }

                field = value
            }


        override fun subscribe_update(update_func: KFunction6<Int, Int, Int, Int, Int, Int, Unit>) {
            _update_func = update_func
        }

        override fun unsubscribe_update() {
            _update_func = null
        }
    }
}