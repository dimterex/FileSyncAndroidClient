package net.dimterex.sync_client.ui.folder.sync.adapter

import android.R
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.obsez.android.lib.filechooser.ChooserDialog
import kotlinx.android.synthetic.main.settings_folder_config_item.view.*
import net.dimterex.sync_client.data.entries.FolderMappingLocalModel
import net.dimterex.sync_client.entity.FolderSelectModel
import net.dimterex.sync_client.ui.adapter.BaseListAdapter
import net.dimterex.sync_client.ui.adapter.BaseViewHolder


class FolderSelectionAdapter() : BaseListAdapter<FolderSelectionViewHolder, FolderSelectModel>() {

    private var _folderSelectionViewHolder: FolderSelectionViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderSelectionViewHolder {
        _folderSelectionViewHolder = FolderSelectionViewHolder(
            LayoutInflater.from(parent.context).inflate(net.dimterex.sync_client.R.layout.settings_folder_config_item, parent, false),
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

        itemView.inside_folder.text = repo.folFolderMappingLocalModel.inside_folder

        val combobox = itemView.row_folder_spinner.editText as? AutoCompleteTextView;

        combobox?.setAdapter(adapter)

        if (repo.folders.contains(repo.folFolderMappingLocalModel.outside_folder))
        {
            combobox?.setText(repo.folFolderMappingLocalModel.outside_folder)
        }

        combobox?.onItemClickListener  = FolderSelectionCallback(repo)

        itemView.changeFolderButton.setOnClickListener(openFolderChooser(repo.folFolderMappingLocalModel, itemView))

        itemView.deleteFolderButton.setOnClickListener { view ->
            removeFunc.invoke(repo)
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
    AdapterView.OnItemClickListener {

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        selectedItemChange.folFolderMappingLocalModel.outside_folder = selectedItemChange.folders[p2]
    }
}

