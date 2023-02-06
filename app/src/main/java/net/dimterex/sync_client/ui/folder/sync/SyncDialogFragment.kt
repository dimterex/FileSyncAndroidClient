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

    var expandableListAdapter: CustomExpandableListAdapter? = null
    private val TAG = this::class.java.name

    override fun initPresenter(): SyncDialogPresenter = SyncDialogPresenter(this)

    override fun layoutId(): Int = R.layout.sync_dialog_fragment

    override fun initView() {
        btn_apply_sync.isEnabled = false

        expandableListAdapter = CustomExpandableListAdapter(context!!)
        settingsConnectionPanel.setAdapter(expandableListAdapter)
    }

    override fun update_added_files(addedFiles: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.added_count), addedFiles.size.toString())
        update(syncStateModel, addedFiles)
    }

    override fun update_removed_files(removedFiles: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.removed_count), removedFiles.size.toString())
        update(syncStateModel, removedFiles)
    }

    override fun update_uploaded_files(uploadedFiles: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.uploaded_count), uploadedFiles.size.toString())
        update(syncStateModel, uploadedFiles)
    }

    override fun update_update_files(updatedFiles: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.updated_count), updatedFiles.size.toString())
        update(syncStateModel, updatedFiles)
    }

    override fun update_server_removed_files(serverRemoved: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.server_removed_count), serverRemoved.size.toString())
        update(syncStateModel, serverRemoved)
    }

    override fun update_database_added_files(databaseAdded: List<String>) {
        val syncStateModel = SyncStateModel(resources.getString(R.string.database_added_count), databaseAdded.size.toString())
        update(syncStateModel, databaseAdded)
    }

    override fun enable_view() {

        btn_apply_sync.text = resources.getString(R.string.btn_sync)
        btn_apply_sync.setOnClickListener {
            presenter.apply()
            dialog?.dismiss()
        }
        btn_apply_sync.isEnabled = true
    }

    private fun update(synsStateModel: SyncStateModel, children: List<String>){
        if (expandableListAdapter == null)
            return

        expandableListAdapter!!.add(synsStateModel, children)
        expandableListAdapter!!.notifyDataSetChanged()
    }
}