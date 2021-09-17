package net.dimterex.sync_client.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

interface ScopeFactory {

    fun getScope() : CoroutineScope

    class Impl : ScopeFactory {
        override fun getScope() : CoroutineScope {
            return  CoroutineScope(Dispatchers.IO + Job())
        }
    }
}