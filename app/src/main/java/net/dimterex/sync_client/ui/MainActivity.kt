package net.dimterex.sync_client.ui

import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        val STORAGE_PERMISSION_CODE = 101
        checkPermission(WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@MainActivity, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this@MainActivity, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }
}
