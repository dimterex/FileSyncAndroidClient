package net.dimterex.sync_client.ui.folder.exit

import kotlinx.android.synthetic.main.exit_fragment_main.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.presenter.base.BaseView
import net.dimterex.sync_client.presenter.menu.exit.ExitPresenter
import net.dimterex.sync_client.ui.base.BaseFragment
import kotlin.system.exitProcess

class ExitFragment : BaseFragment<ExitPresenter>(), ExitView {

    override fun initPresenter(): ExitPresenter = ExitPresenter(this)

    override fun layoutId(): Int = R.layout.exit_fragment_main

    override fun initView() {
        btn_exit.setOnClickListener {
            activity?.finish();
            exitProcess(0);
        }
    }
}

interface ExitView : BaseView {

}