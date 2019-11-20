package net.dimterex.sync_client.data.settings

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import net.dimterex.sync_client.data.settings.local.SettingsDao
import net.dimterex.sync_client.data.settings.local.toLocalModel
import net.dimterex.sync_client.entity.Settings
import net.dimterex.sync_client.usecase.repos.ISettingsSource

class SettingsDaoImpl(private val repoDao: SettingsDao) : ISettingsSource {
    private var _settings : Settings? = null

    override fun load_settings() {
        var result = repoDao.selectById(0)
        _settings = result?.mapToDomain()
    }

    override fun get_settings(): Settings {
        if (_settings == null)
            _settings = Settings()

        return _settings!!
    }

    override fun save(repo: Settings) = CoroutineScope(IO).launch {
        repoDao.update(repo.toLocalModel())
    }
}