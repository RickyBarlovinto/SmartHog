package com.example.smart_hog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.smart_hog.databinding.ActivityMainBinding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.material.icons.automirrored.outlined.ListAlt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var selectedNavId by mutableStateOf(R.id.navigation_menu)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupComposeBottomNav()
    }

    private fun setupComposeBottomNav() {
        val navItems = listOf(
            NavItem(R.id.navigation_dashboard, "Dashboard", Icons.Outlined.Dashboard),
            NavItem(R.id.navigation_feed, "Feeding", Icons.Outlined.Restaurant),
            NavItem(R.id.navigation_menu, "Home", Icons.Outlined.Home),
            NavItem(R.id.navigation_monitor, "Monitor", Icons.Outlined.Monitor),
            NavItem(R.id.navigation_analytics, "Data Analytics", Icons.Outlined.Analytics)
        )

        binding.composeBottomNav.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CustomBottomNavigation(
                    items = navItems,
                    selectedId = selectedNavId,
                    onItemSelected = { item ->
                        if (navController.currentDestination?.id != item.id) {
                            navController.navigate(item.id)
                        }
                    }
                )
            }
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Handle Visibility and Selection of Navigation Bar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedNavId = destination.id
            binding.composeBottomNav.visibility = when (destination.id) {
                R.id.navigation_dashboard,
                R.id.navigation_feed,
                R.id.navigation_monitor,
                R.id.navigation_analytics -> View.VISIBLE
                else -> View.GONE // GONE when on navigation_menu (Home)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
