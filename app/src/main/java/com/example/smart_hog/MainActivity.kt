package com.example.smart_hog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    private lateinit var navCard: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navView = findViewById(R.id.bottom_nav_view)
        navCard = findViewById(R.id.nav_card)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setOnItemSelectedListener { item ->
            val currentId = navController.currentDestination?.id
            if (currentId != item.itemId) {
                val loading = LoadingUtils.showLoading(this)
                Handler(Looper.getMainLooper()).postDelayed({
                    loading.dismiss()
                    navController.navigate(item.itemId)
                }, 800)
            }
            true
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val loading = LoadingUtils.showLoading(this@MainActivity)
                Handler(Looper.getMainLooper()).postDelayed({
                    loading.dismiss()
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                }, 600)
            }
        })

        // Visibility Control: Hide Nav Bar on Menu and other settings pages
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_dashboard,
                R.id.navigation_feed,
                R.id.navigation_health,
                R.id.navigation_analytics -> {
                    navCard.visibility = View.VISIBLE
                }
                else -> {
                    navCard.visibility = View.GONE
                }
            }
        }
    }
}
