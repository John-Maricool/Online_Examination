package com.maricoolsapps.adminpart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        setSupportActionBar(toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setSupportActionBar(toolbar)

        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout)
        navigation_drawer.setupWithNavController(navController)

        val menu = navigation_drawer.menu
        menu.findItem(R.id.log_out).setOnMenuItemClickListener {
            logOut()
        }
    }

    private fun logOut(): Boolean {
        showDialogBox()
        return true
    }

    private fun showDialogBox() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Log Out")
        dialog.setCancelable(false)
        dialog.setMessage("Are you sure you want to log out?")
        dialog.setPositiveButton("Yes"){_,_ ->
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        dialog.setNegativeButton("No"){theDialog,_ ->
            theDialog.cancel()
        }
        val alert = dialog.create()
        alert.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout) || super.onSupportNavigateUp()
    }
}