package net.dimterex.sync_client.entity

data class EventDto(
    val id: Long,
    val repoName: String,
    val ownerName: String,
    val description: String = ""
)