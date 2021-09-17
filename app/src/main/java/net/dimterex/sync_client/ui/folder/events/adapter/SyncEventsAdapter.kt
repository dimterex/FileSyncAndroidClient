package net.dimterex.sync_client.ui.folder.sync.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.item_log.view.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.ui.adapter.BaseListAdapter
import net.dimterex.sync_client.ui.adapter.BaseViewHolder
import net.dimterex.sync_client.ui.formatter.FileStatusFormatter

class SyncEventsAdapter(private val repoPressedListener: (id: String) -> Unit, private val _resources: Resources) : BaseListAdapter<LogViewHolder, FileSyncState>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder =
        LogViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false),
            FileStatusFormatter(_resources),
            repoPressedListener
        )

    fun update(repos: List<FileSyncState>) {
        update(repos, LogHousesCallback(items, repos))
    }

    override fun add(newItem: FileSyncState) {

        items.add(newItem)
        notifyDataSetChanged()
    }
}

class LogViewHolder(view: View, val fileStatusFormatter: FileStatusFormatter, private val listener: (id: String) -> Unit) : BaseViewHolder<FileSyncState>(view) {
    override fun bind(items: List<FileSyncState>, position: Int) {
        val repo = items[position]
        itemView.item_log_name.text = repo.id
        itemView.item_log_details.text = fileStatusFormatter.format(repo.details)
        itemView.setOnClickListener { listener(repo.id) }
    }
}

class LogHousesCallback(private val oldList: List<FileSyncState>, private val newList: List<FileSyncState>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].details == newList[newItemPosition].details
}
