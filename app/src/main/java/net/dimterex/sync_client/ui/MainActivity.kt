package net.dimterex.sync_client.ui

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import net.dimterex.sync_client.R
import net.dimterex.sync_client.presenter.main.MainPresenter
import net.dimterex.sync_client.presenter.main.MainView
import net.dimterex.sync_client.ui.base.BaseActivity

class MainActivity : BaseActivity<MainPresenter>(), MainView {

    private val TAG = this::class.java.name

    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration


    override fun initPresenter(): MainPresenter = MainPresenter(this)

    override fun layoutId(): Int = R.layout.activity_main

    override fun initView() {
        navController = findNavController(R.id.main_nav_host)
        initToolbar()
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

    private fun initToolbar(){
        navigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    companion object{
        private const val STORAGE_PERMISSION_CODE = 101
    }
}
