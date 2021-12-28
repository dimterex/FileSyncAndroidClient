package net.dimterex.sync_client.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import net.dimterex.sync_client.presenter.base.BasePresenter
import net.dimterex.sync_client.presenter.base.BaseView

abstract class BaseFragment<T: BasePresenter> : Fragment(), BaseView {

    lateinit var presenter: T

    abstract fun initPresenter(): T

    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(layoutId(), container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = initPresenter()
        presenter.onCreate(arguments)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}