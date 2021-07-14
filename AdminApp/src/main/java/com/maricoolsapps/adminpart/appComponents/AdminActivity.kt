package com.maricoolsapps.adminpart.appComponents

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.adminpart.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_admin.*
import javax.inject.Inject


@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    @Inject
    lateinit var auth: FirebaseAuth

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
        menu.findItem(R.id.id).setOnMenuItemClickListener {
            showAdminIdAndCopy()
        }
    }

    private fun showAdminIdAndCopy(): Boolean {
        val id = auth.currentUser.uid
        Toast.makeText(this, "Your id is $id", Toast.LENGTH_SHORT).show()
        val manager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("id", id)
        manager.setPrimaryClip(clipData)
        Toast.makeText(this, "Your Id is copied", Toast.LENGTH_SHORT).show()
        return true
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
        dialog.setPositiveButton("Yes"){ _, _ ->
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        dialog.setNegativeButton("No"){ theDialog, _ ->
            theDialog.cancel()
        }
        val alert = dialog.create()
        alert.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawer_layout) || super.onSupportNavigateUp()
    }
}