package net.dimterex.sync_client.usecase.repos

import kotlinx.coroutines.Job
import net.dimterex.sync_client.entity.Settings

interface ISettingsSource {

    fun load_settings()

    fun save(repo: Settings): Job

    fun get_settings(): Settings
}