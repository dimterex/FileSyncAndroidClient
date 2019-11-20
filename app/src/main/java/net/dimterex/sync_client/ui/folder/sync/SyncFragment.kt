package net.dimterex.sync_client.ui.folder.sync

import android.os.Bundle
import android.view.View
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

//    private lateinit var adapter: RepoAdapter
    private lateinit var controller: NavController

    override fun navigateRepoDetails(id: Long) {
        val args = Bundle()
        args.putLong(resources.getString(R.string.key_repo_id), id)
        controller.navigate(R.id.action_homeFragment_to_reposDetailsFragment, args)
    }

    override var repos: List<EventDto> = mutableListOf()
        set(value) {
//            adapter.update(value)
//            repos_list.scrollToPosition(repos.size)
            field = value
        }

    override fun initPresenter(): SyncPresenter = SyncPresenter(this)

    override fun layoutId(): Int = R.layout.fragment_repos

    override fun initView() {
        controller =  Navigation
            .findNavController(activity as MainActivity, R.id.main_nav_host)
//        adapter = RepoAdapter(presenter::onRepoPressed)

        sync_button.setOnClickListener {view ->
            presenter.sync_execute()
        }

//        repos_list.layoutManager = LinearLayoutManager(repos_list.context)
//        repos_list.adapter = adapter
//        repos_list.addOnScrollListener(OnScrolldedToBottomListener(presenter::onScrolledToBottom))
    }

    override fun event_change(string: String) {
            // TODO: сделать отдельную вбьюху по аналогии с репо и профайл
      //  repos_toolbar.text = string
    }

    override fun showMenu() {
        activity?.main_bottom_navigation?.visibility = View.VISIBLE
    }
}
