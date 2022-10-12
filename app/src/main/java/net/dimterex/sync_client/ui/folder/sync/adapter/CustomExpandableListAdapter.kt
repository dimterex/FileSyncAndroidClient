package net.dimterex.sync_client.ui.folder.sync.adapter

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.sync_filestate_item.view.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.SyncStateModel


class CustomExpandableListAdapter(private val context: Context) : BaseExpandableListAdapter() {
    private val expandableListTitle: ArrayList<SyncStateModel> = ArrayList<SyncStateModel>()
    private val expandableListDetail: HashMap<SyncStateModel, List<String>> = HashMap<SyncStateModel, List<String>>()

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

        var convertView: View? = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String

        if (convertView == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.sync_state_element_list_item, null)
        }
        val expandedListTextView = convertView?.findViewById(R.id.file_path) as TextView
        expandedListTextView.text = expandedListText
        return convertView
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
        var convertView: View? = convertView
        val listTitle = getGroup(listPosition) as SyncStateModel

        if (convertView == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView =  layoutInflater.inflate(R.layout.sync_state_element, null)
        }
        val listTitleTextView = convertView!!.findViewById(R.id.sync_state_element_title) as TextView
        listTitleTextView.text = listTitle.name

        val countTextView = convertView.findViewById(R.id.added_count_textview) as TextView
        countTextView.text = listTitle.count
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

    fun add(syncStateModel: SyncStateModel, children: List<String>) {
        expandableListTitle.add(syncStateModel)
        expandableListDetail[syncStateModel] = children
    }
}