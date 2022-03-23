package com.maricoolsapps.studentapp.application

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.utils.interfaces.AlertDialogListener
import com.maricoolsapps.utils.others.startAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AlertDialogListener {

    private lateinit var navController: NavController

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        NavigationUI.setupActionBarWithNavController(this, navController,drawer_layout)
        NavigationUI.setupWithNavController(navigation_drawer,  navController)
        // navigation_drawer.setupWithNavController(navController)

        val menu = navigation_drawer.menu
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        menu.findItem(R.id.log_out).setOnMenuItemClickListener {
            logOut()
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.studentLogInFragment -> {
                    toolbar.visibility = View.GONE
                    drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                R.id.studentSignup -> {
                    toolbar.visibility = View.GONE
                    drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    toolbar.visibility = View.VISIBLE
                    drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }
    }

    private fun logOut(): Boolean {
        showDialogBox()
        return true
    }

    private fun showDialogBox() {
        this.startAlertDialog(
            title = "Log Out",
            message = "Are you sure you want to log out?",
            listener = this
        )
    }

    override fun onSupportNavigateUp(): Boolean {
         onBackPressed()
        return true
      // return NavigationUI.navigateUp(navController, drawer_layout)
    }

    override fun onPositiveListener(dialog: DialogInterface, text: String) {
        auth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onNegativeListener(dialog: DialogInterface, text: String) {
        dialog.cancel()
    }
}