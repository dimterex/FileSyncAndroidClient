package net.dimterex.sync_client.ui.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<VH: BaseViewHolder<T>, T: Any> :
    RecyclerView.Adapter<VH>(),
    BaseListAdapterInterface<T> {

    val items = mutableListOf<T>()

    override fun getItemCount(): Int = items.size

    override fun update(newList: List<T>, callBack: DiffUtil.Callback) {
        val diffResult = DiffUtil.calculateDiff(callBack)
        items.clear()
        items.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun add(newItem: T){
        items.add(newItem)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items, position)
    }
}

abstract class BaseViewHolder<T>(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(items: List<T>, position: Int)
}