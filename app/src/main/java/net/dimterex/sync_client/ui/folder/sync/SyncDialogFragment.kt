package net.dimterex.sync_client.ui.folder.sync

import android.content.res.Resources
import android.widget.ExpandableListView
import kotlinx.android.synthetic.main.sync_dialog_fragment.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.SyncStateModel
import net.dimterex.sync_client.presenter.menu.sync.SyncDialogPresenter
import net.dimterex.sync_client.presenter.menu.sync.SyncDialogView
import net.dimterex.sync_client.ui.base.BaseDialogFragment
import net.dimterex.sync_client.ui.folder.sync.adapter.CustomExpandableListAdapter


class SyncDialogFragment () : BaseDialogFragment<SyncDialogPresenter>(), SyncDialogView {

    var expandableListView: ExpandableListView? = null
    var expandableListAdapter: CustomExpandableListAdapter? = null
    var expandableListTitle: List<SyncStateModel>? = null
    var expandableListDetail: HashMap<SyncStateModel, List<String>>? = null

    override fun initPresenter(): SyncDialogPresenter = SyncDialogPresenter(this)

    override fun layoutId(): Int = R.layout.sync_dialog_fragment

    override fun initView() {
//        added_count_textview.text = "-"
//        removed_count_textview.text = "-"
//        uploaded_count_textview.text = "-"
//        updated_count_textview.text = "-"
//        server_removed_count_textview.text = "-"
//        database_added_count_textview.text = "-"
        btn_apply_sync.isEnabled = false

//        expandableListDetail = ExpandableListDataPump().getData();
//        expandableListTitle = expandableListDetail!!.keys.toList()

        expandableListAdapter = CustomExpandableListAdapter(context!!);
        settingsConnectionPanel.setAdapter(expandableListAdapter);

//        val cricket: ArrayList<String> = ArrayList()
//        cricket.add("India")
//        cricket.add("Pakistan")
//        cricket.add("Australia")
//        cricket.add("England")
//        cricket.add("South Africa")
//
//        update_added_files(cricket)

        settingsConnectionPanel.setOnGroupExpandListener(ExpandableListView.OnGroupExpandListener() {
//            println(expandableListTitle!![it].name + " List Expanded.")

//            Toast.makeText(context, ).show();
        });

        settingsConnectionPanel.setOnGroupCollapseListener(ExpandableListView.OnGroupCollapseListener() {
//            println(expandableListTitle!![it].name + " List Collapsed.")
//            Toast.makeText(context,expandableListTitle.get(it) + " List Collapsed.",Toast.LENGTH_SHORT).show();
        });


//        settingsConnectionPanel.setOnChildClickListener(ExpandableListView.OnChildClickListener() {
//                parent: ExpandableListView, v: View, groupPosition: Int, childPosition: Int, id: Long ->


//                Toast.makeText(
//                    context,
//                    expandableListTitle.get(groupPosition) + " -> "+ expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition),
//                    Toast.LENGTH_SHORT
//                ).show();
//        });

    }

    override fun update_added_files(addedFiles: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.added_count), addedFiles.size.toString())
        expandableListAdapter!!.add(syncStateModel, addedFiles)
        expandableListAdapter?.notifyDataSetChanged()
    }

    override fun update_removed_files(removedFiles: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.removed_count), removedFiles.size.toString())
        expandableListAdapter!!.add(syncStateModel, removedFiles)
        expandableListAdapter?.notifyDataSetChanged()
    }

    override fun update_uploaded_files(uploadedFiles: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.uploaded_count), uploadedFiles.size.toString())
        expandableListAdapter!!.add(syncStateModel, uploadedFiles)
        expandableListAdapter?.notifyDataSetChanged()
    }

    override fun update_update_files(updatedFiles: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.updated_count), updatedFiles.size.toString())
        expandableListAdapter!!.add(syncStateModel, updatedFiles)
        expandableListAdapter?.notifyDataSetChanged()
    }

    override fun update_server_removed_files(serverRemoved: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.server_removed_count), serverRemoved.size.toString())
        expandableListAdapter!!.add(syncStateModel, serverRemoved)
        expandableListAdapter?.notifyDataSetChanged()
    }

    override fun update_database_added_files(databaseAdded: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.database_added_count), databaseAdded.size.toString())
        expandableListAdapter!!.add(syncStateModel, databaseAdded)
        expandableListAdapter?.notifyDataSetChanged()
    }

    override fun enable_view() {

        btn_apply_sync.text = resources.getString(R.string.btn_sync)
        btn_apply_sync.setOnClickListener {
            presenter.apply()
            dialog?.dismiss()
        }
        btn_apply_sync.isEnabled = true
    }
}