package net.dimterex.sync_client.ui.folder.settings

import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.obsez.android.lib.filechooser.ChooserDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.data.entries.ConnectionsLocalModel
import net.dimterex.sync_client.presenter.menu.settings.SettingsPresenter
import net.dimterex.sync_client.presenter.menu.settings.SettingsView
import net.dimterex.sync_client.ui.base.BaseFragment
import java.io.File

class SettingsFragment : BaseFragment<SettingsPresenter>(), SettingsView {
    private var _sync_folder : File = File(String())

    private var _port: EditText? = null
    private var _ip_address: EditText? = null
//    private var _ip_address : String = String()
//    private var _port : Int = 0


    override var profile: ConnectionsLocalModel? = null
    set(value) {

        if (value != null) {
//            _sync_folder = File(value.defaultFolder)
//
//            set_path(value.defaultFolder)
            btnChoose.setOnClickListener(openFolderChooser(""))

            _ip_address?.setText(value.ip_address,  TextView.BufferType.EDITABLE)
            _port?.setText(value.ip_port.toString())
        }

        field = value
    }

    override fun initPresenter(): SettingsPresenter = SettingsPresenter(this)

    override fun layoutId(): Int = R.layout.fragment_profile

    override fun initView() {
        _ip_address = ip_address
        _port = port
    }

    override fun showMenu() {
        activity?.main_bottom_navigation?.visibility = View.VISIBLE
    }

    override fun get_ip_port(): Int {
        return _port?.text.toString().toInt()
    }

    override fun get_sync_folder(): File {
       return _sync_folder
    }

    override fun get_ip_address(): String  {
        return _ip_address?.text.toString()
    }

    private fun openFolderChooser(sync_folder: String): View.OnClickListener? {
        return View.OnClickListener {  ChooserDialog(this@SettingsFragment.requireActivity())
            .withFilter(true, false)
            .withStartFile(sync_folder)
            .withChosenListener { path, pathFile ->
                _sync_folder = pathFile
                set_path(path)
            }
            .build()
            .show()
        }
    }

    private fun set_path(path: String){
        textDirectory.text = "FOLDER: $path"
    }
}
