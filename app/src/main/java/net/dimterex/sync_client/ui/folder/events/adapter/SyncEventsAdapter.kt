package net.dimterex.sync_client.ui.folder.sync.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.sync_filestate_item.view.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.ui.adapter.BaseListAdapter
import net.dimterex.sync_client.ui.adapter.BaseViewHolder
import net.dimterex.sync_client.ui.formatter.FileStatusFormatter
import net.dimterex.sync_client.ui.formatter.FileSyncTypeFormatter

class SyncEventsAdapter(private val repoPressedListener: (id: String) -> Unit, private val _resources: Resources) : BaseListAdapter<LogViewHolder, FileSyncState>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder =

        LogViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.sync_filestate_item, parent, false),
            FileStatusFormatter(_resources),
            FileSyncTypeFormatter(_resources),
            repoPressedListener
        )

    fun update(repos: List<FileSyncState>) {
        update(repos, LogHousesCallback(items, repos))
    }
}

class LogViewHolder(view: View,
                    val fileStatusFormatter: FileStatusFormatter,
                    val fileSyncTypeFormatter: FileSyncTypeFormatter,
                    private val listener: (id: String) -> Unit) : BaseViewHolder<FileSyncState>(view) {

    override fun bind(items: List<FileSyncState>, position: Int) {
        val repo = items[position]

        itemView.item_log_order.text = repo.number
        itemView.item_log_state.text = fileSyncTypeFormatter.format(repo.state)
        itemView.item_log_name.text = repo.inside_path
        itemView.item_log_details.text = fileStatusFormatter.format(repo.process)

        itemView.setOnClickListener { listener(repo.inside_path) }
    }
}

class LogHousesCallback(private val oldList: List<FileSyncState>, private val newList: List<FileSyncState>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].inside_path == newList[newItemPosition].inside_path

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].process == newList[newItemPosition].process
}
