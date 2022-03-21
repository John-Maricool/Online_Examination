package com.maricoolsapps.adminpart.appComponents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maricoolsapps.adminpart.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var bottomBar: BottomNavigationView
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        bottomBar = findViewById(R.id.bottom_nav)
        NavigationUI.setupWithNavController(bottomBar, navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.loginFragment -> {
                    toolbar.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                }
                R.id.signUpFragment -> {
                    toolbar.visibility = View.GONE
                    bottomBar.visibility = View.GONE
                }
                else -> {
                    toolbar.visibility = View.VISIBLE
                    bottomBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}