package net.dimterex.sync_client.ui

import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.presenter.main.MainPresenter
import net.dimterex.sync_client.presenter.main.MainView
import net.dimterex.sync_client.ui.base.BaseActivity

class MainActivity : BaseActivity<MainPresenter>(), MainView {

    override fun initPresenter(): MainPresenter = MainPresenter(this)

    override fun layoutId(): Int = R.layout.activity_main

    override fun showError(error: Throwable) {}

    override fun initView() {

        val navController = Navigation.findNavController(this, R.id.main_nav_host)
        main_bottom_navigation.setupWithNavController(navController)
    }

    override fun showMenu() {
        main_bottom_navigation.visibility = View.VISIBLE
    }
}
