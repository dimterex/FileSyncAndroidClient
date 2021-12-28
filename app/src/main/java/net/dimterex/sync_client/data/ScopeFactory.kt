package net.dimterex.sync_client.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

interface ScopeFactory {

    fun getScope() : CoroutineScope
    fun getMainScope() : CoroutineScope

    class Impl : ScopeFactory {

        private var mainScope : Int = 0
        private var scope : Int = 0

        override fun getScope() : CoroutineScope {
            println("SCOPE = " + scope++)
            return CoroutineScope(Dispatchers.IO + Job())
        }

        override fun getMainScope() : CoroutineScope {
            println("MAIN SCOPE = " + mainScope++)
            return CoroutineScope(Dispatchers.Main)
        }
    }
}