package net.dimterex.sync_client.ui.folder.sync

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_repos.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.EventDto
import net.dimterex.sync_client.presenter.menu.sync.SyncPresenter
import net.dimterex.sync_client.presenter.menu.sync.SyncView
import net.dimterex.sync_client.ui.MainActivity
import net.dimterex.sync_client.ui.base.BaseFragment

class SyncFragment : BaseFragment<SyncPresenter>(), SyncView {

    private lateinit var controller: NavController


    override fun initPresenter(): SyncPresenter = SyncPresenter(this)

    override fun layoutId(): Int = R.layout.fragment_repos

    override fun initView() {
        controller =  Navigation
            .findNavController(activity as MainActivity, R.id.main_nav_host)

        sync_button.setOnClickListener {view ->
            presenter.sync_execute()
        }

//        repos_list.layoutManager = LinearLayoutManager(repos_list.context)
//        repos_list.adapter = adapter
//        repos_list.addOnScrollListener(OnScrolldedToBottomListener(presenter::onScrolledToBottom))
    }

    override fun showMenu() {
        activity?.main_bottom_navigation?.visibility = View.VISIBLE
    }
}
