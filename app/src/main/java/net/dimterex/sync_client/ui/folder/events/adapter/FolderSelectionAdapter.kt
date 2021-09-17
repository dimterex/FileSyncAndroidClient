package net.dimterex.sync_client.ui.folder.sync.adapter

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.obsez.android.lib.filechooser.ChooserDialog
import kotlinx.android.synthetic.main.folder_select_item_log.view.*
import net.dimterex.sync_client.data.entries.FolderMappingLocalModel
import net.dimterex.sync_client.entity.FolderSelectModel
import net.dimterex.sync_client.ui.adapter.BaseListAdapter
import net.dimterex.sync_client.ui.adapter.BaseViewHolder


class FolderSelectionAdapter(private val data: Array<String>) : BaseListAdapter<FolderSelectionViewHolder, FolderSelectModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderSelectionViewHolder =
        FolderSelectionViewHolder(
            LayoutInflater.from(parent.context).inflate(net.dimterex.sync_client.R.layout.folder_select_item_log, parent, false),
            data
        )

    fun update(repos: List<FolderSelectModel>) {
//        update(repos, AsyncListDiffer<FolderSelectModel>())
    }

    override fun add(newItem: FolderSelectModel) {
        items.add(newItem)
        notifyDataSetChanged()
    }
}

class FolderSelectionViewHolder(val view: View, val data: Array<String>) : BaseViewHolder<FolderSelectModel>(view) {

    override fun bind(items: List<FolderSelectModel>, position: Int) {
        val repo = items[position]
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(itemView.context, R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        itemView.row_folder_spinner.adapter = adapter
        itemView.inside_folder.text = repo.folFolderMappingLocalModel.inside_folder

        // выделяем элемент
//        itemView.row_folder_spinner.setSelection(2);
        // устанавливаем обработчик нажатия
        itemView.row_folder_spinner.setOnItemSelectedListener(FolderSelectionCallback(repo))

        itemView.changeFolderButton.setOnClickListener(openFolderChooser(repo.folFolderMappingLocalModel, itemView))

//        itemView.setOnClickListener { listener(repo.id) }
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

        Toast.makeText(p0?.context, "Position = " + p2, Toast.LENGTH_SHORT).show();
        selectedItemChange.folFolderMappingLocalModel.outside_folder = selectedItemChange.folders[p2]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}

