package net.dimterex.sync_client.modules

import kotlin.reflect.KFunction0
import kotlin.reflect.KFunction4

interface SyncStateManager {
    fun subscribe_apply(kFunction0: KFunction0<Unit>)
    fun subscribe_update(kFunction0: KFunction4<Int, Int,Int,Int, Unit>)

    fun unsubscribe_update()


    fun update_state(addedFilesCount: Int, removedFilesCount: Int, uploadedFilesCount: Int, updatedFilesCount: Int)
    fun apply()

    class Impl() : SyncStateManager{
        private var _apply_func: KFunction0<Unit>? = null
        private var _update_func: KFunction4<Int, Int,Int,Int, Unit>? = null

        override fun subscribe_apply(apply_func: KFunction0<Unit>) {
            _apply_func = apply_func
        }

        override fun subscribe_update(update_func: KFunction4<Int, Int,Int,Int, Unit>) {
            _update_func = update_func
        }

        override fun unsubscribe_update() {
            _update_func = null
        }

        override fun update_state(addedFilesCount: Int, removedFilesCount: Int, uploadedFilesCount: Int, updatedFilesCount: Int) {
            _update_func?.invoke(addedFilesCount, removedFilesCount, uploadedFilesCount, updatedFilesCount)
        }

        override fun apply() {
            _apply_func?.invoke()
        }
    }
}