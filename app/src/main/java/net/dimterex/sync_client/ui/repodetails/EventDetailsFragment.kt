package net.dimterex.sync_client.ui.repodetails

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_repo_details.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.entity.FileSyncState
import net.dimterex.sync_client.presenter.base.HiddenMenuScreen
import net.dimterex.sync_client.presenter.repodetails.EventDetailsPresenter
import net.dimterex.sync_client.presenter.repodetails.EventDetailsView
import net.dimterex.sync_client.ui.MainActivity
import net.dimterex.sync_client.ui.base.BaseFragment

class EventDetailsFragment :
    BaseFragment<EventDetailsPresenter>(),
    EventDetailsView,
    HiddenMenuScreen {

    private lateinit var controller: NavController

    override var repo: FileSyncState? = null
        set(value) {
//            repo_details_repo_name.text = value?.repoName ?: ""
//            repo_details_description.text = value?.description ?: ""
//            repo_details_owner_name.text = value?.ownerName ?: ""
            field = value
        }

    override fun initPresenter(): EventDetailsPresenter = EventDetailsPresenter(this, resources)

    override fun layoutId(): Int = R.layout.fragment_repo_details

    override fun initView() {
        controller =  Navigation
            .findNavController(activity as MainActivity, R.id.main_nav_host)
        repo_details_button_back.setOnClickListener { presenter.onBackButtonPressed() }
    }

    override fun hideMenu() {
        activity!!.main_bottom_navigation?.visibility = View.GONE
    }

    override fun showMenu() {}

    override fun back() {
        controller.navigateUp()
    }
}
