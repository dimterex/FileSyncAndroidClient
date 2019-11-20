package net.dimterex.sync_client.data

interface DataMapper<TDomain> {

    fun mapToDomain() : TDomain
}