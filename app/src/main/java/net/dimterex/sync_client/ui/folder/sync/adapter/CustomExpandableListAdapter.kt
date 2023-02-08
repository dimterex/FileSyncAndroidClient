package net.dimterex.sync_client.ui.folder.sync.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.SyncStateModel


class CustomExpandableListAdapter(private val context: Context) : BaseExpandableListAdapter() {
    private val expandableListTitle: ArrayList<SyncStateModel> = ArrayList<SyncStateModel>()
    private val expandableListDetail: HashMap<SyncStateModel, List<String>> = HashMap<SyncStateModel, List<String>>()
    private val TAG = this::class.java.name

    fun clear() {
        expandableListTitle.clear()
        expandableListDetail.clear()
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return expandableListDetail[expandableListTitle[listPosition]]!!.get(expandedListPosition)
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int, expandedListPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup?
    ): View {
        Log.d(TAG, "getChildView")

        var newView: View? = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String

        if (newView == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            newView = layoutInflater.inflate(R.layout.sync_state_element_list_item, null)
        }
        val expandedListTextView = newView!!.findViewById(R.id.file_path) as TextView
        expandedListTextView.text = expandedListText
        return newView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return expandableListDetail[expandableListTitle[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return expandableListTitle[listPosition]
    }

    override fun getGroupCount(): Int {
        return expandableListTitle.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(
        listPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup?
    ): View {
        Log.d(TAG, "getGroupView")
        var newView: View? = convertView
        val listTitle = getGroup(listPosition) as SyncStateModel

        if (newView == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            newView =  layoutInflater.inflate(R.layout.sync_state_element, null)
        }
        val listTitleTextView = newView!!.findViewById(R.id.sync_state_element_title) as TextView
        listTitleTextView.text = listTitle.name

        val countTextView = newView.findViewById(R.id.added_count_textview) as TextView
        countTextView.text = listTitle.count
        Log.d(TAG, "${countTextView.text}")
        Log.d(TAG, "${newView}")
        return newView
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

    fun add(syncStateModel: SyncStateModel, children: List<String>) {
        expandableListTitle.add(syncStateModel)
        expandableListDetail[syncStateModel] = children
    }
}