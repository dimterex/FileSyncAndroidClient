package net.dimterex.sync_client.ui.folder.sync.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.synthetic.main.item_log.view.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.EventDto
import net.dimterex.sync_client.ui.adapter.BaseListAdapter
import net.dimterex.sync_client.ui.adapter.BaseViewHolder

class LogAdapter(private val repoPressedListener: (id: Long) -> Unit) : BaseListAdapter<LogViewHolder, EventDto>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder =
        LogViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false),
            repoPressedListener
        )

    fun update(repos: List<EventDto>) {
        update(repos, LogHousesCallback(items, repos))
    }

    fun add(message: String){

        var tmp = EventDto(0, message, message, message)
        add(tmp)
    }
}

class LogViewHolder(view: View, private val listener: (id: Long) -> Unit) : BaseViewHolder<EventDto>(view) {
    override fun bind(items: List<EventDto>, position: Int) {
        val repo = items[position]
        itemView.item_log_name.text = repo.repoName
        itemView.setOnClickListener { listener(repo.id) }
    }
}

class LogHousesCallback(private val oldList: List<EventDto>, private val newList: List<EventDto>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].repoName == newList[newItemPosition].repoName
}
