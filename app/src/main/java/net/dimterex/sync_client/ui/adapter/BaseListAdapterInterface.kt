package net.dimterex.sync_client.ui.adapter

import androidx.recyclerview.widget.DiffUtil

interface BaseListAdapterInterface<T: Any> {
    fun update(newList: List<T>, callBack: DiffUtil.Callback)

    fun add(newItem: T)

    fun clear()
}