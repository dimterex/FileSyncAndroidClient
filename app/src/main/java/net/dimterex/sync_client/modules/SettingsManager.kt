package net.dimterex.sync_client.modules

import net.dimterex.sync_client.entity.Settings
import net.dimterex.sync_client.usecase.repos.ISettingsSource
import kotlin.concurrent.thread

import kotlin.reflect.KFunction0

interface SettingsManager {

    fun get_settings() : Settings

    fun save_settings()
    fun set_default_folder(path: String)
    fun set_ip_address(getIpAddress: String)
    fun set_ip_port(getIpPort: Int)

    fun add_listener(restart: KFunction0<Unit>)

    class Impl(private val repoSource: ISettingsSource) : SettingsManager {

        private val loaded_settings : Settings = repoSource.get_settings()

        private var _restart: KFunction0<Unit>? = null
        private var _isHostOrPortChange = false

        override fun add_listener(restart: KFunction0<Unit>) {
            _restart = restart
        }

        override fun get_settings() : Settings {
            return loaded_settings
        }

        override fun save_settings() {
            repoSource.save(loaded_settings)

            if (_isHostOrPortChange) {
                thread (true) {
                    _restart?.invoke()
                }
            }
        }

        override fun set_default_folder(path: String) {
            loaded_settings.defaultFolder = path
        }

        override fun set_ip_address(new_ip_address: String) {
            _isHostOrPortChange = loaded_settings.connectionSettings.ip_address != new_ip_address
            loaded_settings.connectionSettings.ip_address = new_ip_address
        }

        override fun set_ip_port(new_ip_port: Int) {
            _isHostOrPortChange = loaded_settings.connectionSettings.port != new_ip_port
            loaded_settings.connectionSettings.port = new_ip_port
        }
    }
}
