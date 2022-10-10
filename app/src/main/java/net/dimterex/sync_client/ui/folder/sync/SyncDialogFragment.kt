package net.dimterex.sync_client.ui.folder.sync

import kotlinx.android.synthetic.main.sync_dialog_fragment.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.presenter.menu.sync.SyncDialogPresenter
import net.dimterex.sync_client.presenter.menu.sync.SyncDialogView
import net.dimterex.sync_client.ui.base.BaseDialogFragment

class SyncDialogFragment : BaseDialogFragment<SyncDialogPresenter>(), SyncDialogView {

    override fun initPresenter(): SyncDialogPresenter = SyncDialogPresenter(this)

    override fun layoutId(): Int = R.layout.sync_dialog_fragment

    override fun initView() {
        added_count_textview.text = "-"
        removed_count_textview.text = "-"
        uploaded_count_textview.text = "-"
        updated_count_textview.text = "-"
        server_removed_count_textview.text = "-"
        database_added_count_textview.text = "-"
        btn_apply_sync.isEnabled = false

    }

    override fun update_added_files(addedFilesCount: Int) {
        added_count_textview.text = addedFilesCount.toString()
    }

    override fun update_removed_files(removedFilesCount: Int) {
        removed_count_textview.text = removedFilesCount.toString()
    }

    override fun update_uploaded_files(uploadedFilesCount: Int) {
        uploaded_count_textview.text = uploadedFilesCount.toString()
    }

    override fun update_update_files(updatedFilesCount: Int) {
        updated_count_textview.text = updatedFilesCount.toString()
    }

    override fun update_server_removed_files(serverRemovedCount: Int) {
        server_removed_count_textview.text = serverRemovedCount.toString()
    }

    override fun update_database_added_files(databaseAddedCount: Int) {
        database_added_count_textview.text = databaseAddedCount.toString()
    }

    override fun enable_view() {
        btn_apply_sync.setOnClickListener {
            presenter.apply()
            dialog?.dismiss()
        }
        btn_apply_sync.isEnabled = true
    }
}