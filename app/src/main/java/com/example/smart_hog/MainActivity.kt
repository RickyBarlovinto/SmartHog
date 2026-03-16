package com.example.smart_hog

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        val navCard: View = findViewById(R.id.nav_card)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // I-sync ang bottom nav sa navigation graph
        navView.setupWithNavController(navController)

        // I-handle ang custom behavior ng sentrong "Menu" button
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_menu -> {
                    navController.navigate(R.id.navigation_menu)
                    true
                }
                R.id.navigation_dashboard -> {
                    navController.navigate(R.id.navigation_dashboard)
                    true
                }
                R.id.navigation_feed -> {
                    navController.navigate(R.id.navigation_feed)
                    true
                }
                R.id.navigation_health -> {
                    navController.navigate(R.id.navigation_health)
                    true
                }
                R.id.navigation_analytics -> {
                    navController.navigate(R.id.navigation_analytics)
                    true
                }
                else -> false
            }
        }

        // Itago/Ipakita ang navigation card depende sa screen
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_menu) {
                navCard.visibility = View.GONE
            } else {
                navCard.visibility = View.VISIBLE
            }
        }
    }
}
