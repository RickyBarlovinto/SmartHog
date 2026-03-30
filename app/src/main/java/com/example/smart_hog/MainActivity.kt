package com.example.smart_hog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
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

        // Custom Navigation with Loading for Bottom Nav
        navView.setOnItemSelectedListener { item ->
            val currentId = navController.currentDestination?.id
            if (currentId != item.itemId) {
                val loading = LoadingUtils.showLoading(this)
                Handler(Looper.getMainLooper()).postDelayed({
                    loading.dismiss()
                    navController.navigate(item.itemId)
                }, 1000)
            }
            true
        }

        // Handle Global Back Button with Loading
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val loading = LoadingUtils.showLoading(this@MainActivity)
                Handler(Looper.getMainLooper()).postDelayed({
                    loading.dismiss()
                    isEnabled = false // Disable this callback to allow default back action
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true // Re-enable for next time
                }, 800)
            }
        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_menu, 
                R.id.navigation_settings, 
                R.id.navigation_about,
                R.id.navigation_privacy_security,
                R.id.navigation_language_region -> {
                    navCard.visibility = View.GONE
                }
                else -> {
                    navCard.visibility = View.VISIBLE
                }
            }
        }
    }
}
