package net.dimterex.sync_client.modules

import kotlin.reflect.KFunction1

interface AvailableFoldersManager {

    val logs: ArrayList<String>

    fun save_event(string: String)
    fun subscribe_added_event(addEventFunc: KFunction1<String, Unit>)
    fun unsubscribe_added_event()

    class Impl(private val _settingsManager: SettingsManager) : AvailableFoldersManager {

        private var _addEventFunc: KFunction1<String, Unit>? = null

        override val logs: ArrayList<String> = ArrayList()

        init {
            _settingsManager.add_listener(this::settingsReaded)
        }

        override fun save_event(string: String) {
            if (logs.contains(string))
                return

            logs.add(string)
            _addEventFunc?.invoke(string)
        }

        override fun subscribe_added_event(addEventFunc: KFunction1<String, Unit>) {
            _addEventFunc = addEventFunc
        }

        override fun unsubscribe_added_event() {
            _addEventFunc = null
        }

        private fun settingsReaded(){
            val folders = _settingsManager.get_folder_mapping()
            folders.forEach { x ->
                if (!logs.contains(x.outside_folder))
                    logs.add(x.outside_folder)
            }
        }
    }
}