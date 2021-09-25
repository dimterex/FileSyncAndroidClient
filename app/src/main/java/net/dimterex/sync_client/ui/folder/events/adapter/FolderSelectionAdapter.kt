package net.dimterex.sync_client.ui.folder.sync.adapter

import android.R
import android.app.AlertDialog
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.obsez.android.lib.filechooser.ChooserDialog
import kotlinx.android.synthetic.main.folder_select_item_log.view.*
import net.dimterex.sync_client.data.entries.FolderMappingLocalModel
import net.dimterex.sync_client.entity.FolderSelectModel
import net.dimterex.sync_client.ui.adapter.BaseListAdapter
import net.dimterex.sync_client.ui.adapter.BaseViewHolder


class FolderSelectionAdapter() : BaseListAdapter<FolderSelectionViewHolder, FolderSelectModel>() {

    private var _folderSelectionViewHolder: FolderSelectionViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderSelectionViewHolder {
        _folderSelectionViewHolder = FolderSelectionViewHolder(
            LayoutInflater.from(parent.context).inflate(net.dimterex.sync_client.R.layout.folder_select_item_log, parent, false),
            this::remove)

        return _folderSelectionViewHolder!!
    }

    override fun add(newItem: FolderSelectModel) {
        items.add(newItem)
        notifyDataSetChanged()
    }

    private fun remove(oldItem: FolderSelectModel) {
        items.remove(oldItem)
        notifyDataSetChanged()
    }
}

class FolderSelectionViewHolder(val view: View, val removeFunc: (oldItem: FolderSelectModel) -> Unit) : BaseViewHolder<FolderSelectModel>(view) {

    override fun bind(items: List<FolderSelectModel>, position: Int) {
        val repo = items[position]

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(itemView.context, R.layout.simple_spinner_item, repo.folders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        itemView.row_folder_spinner.adapter = adapter
        itemView.inside_folder.text = repo.folFolderMappingLocalModel.inside_folder

        val selectedIndex = repo.folders.indexOf(repo.folFolderMappingLocalModel.inside_folder)

        // выделяем элемент
        itemView.row_folder_spinner.setSelection(selectedIndex)

        // устанавливаем обработчик нажатия
        itemView.row_folder_spinner.setOnItemSelectedListener(FolderSelectionCallback(repo))

        itemView.changeFolderButton.setOnClickListener(openFolderChooser(repo.folFolderMappingLocalModel, itemView))

        itemView.inside_folder.setOnLongClickListener { view ->
            AlertDialog.Builder(view.context)
                .setMessage(net.dimterex.sync_client.R.string.remove_it)
                .setPositiveButton(R.string.ok) { _,_ -> removeFunc.invoke(repo) }
                .setNegativeButton(R.string.cancel) {_,_ ->}
                .create()
                .show()

            true
        }
    }

    private fun openFolderChooser(folderMappingLocalModel: FolderMappingLocalModel, context: View): View.OnClickListener? {
        return View.OnClickListener {  ChooserDialog(view.context)
            .withFilter(true, false)
            .withStartFile(folderMappingLocalModel.inside_folder)
            .withChosenListener { path, pathFile ->

                folderMappingLocalModel.inside_folder = path
                context.inside_folder.text = path
            }
            .build()
            .show()
        }
    }
}

class FolderSelectionCallback(val selectedItemChange: FolderSelectModel) :
    AdapterView.OnItemSelectedListener {
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        selectedItemChange.folFolderMappingLocalModel.outside_folder = selectedItemChange.folders[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}

