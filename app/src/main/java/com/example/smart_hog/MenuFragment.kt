package com.example.smart_hog

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dashboard
        view.findViewById<MaterialCardView>(R.id.menu_dashboard)?.setOnClickListener {
            navigateToPageWithLoading { findNavController().navigate(R.id.navigation_dashboard) }
        }

        // Feed
        view.findViewById<MaterialCardView>(R.id.menu_feed)?.setOnClickListener {
            navigateToPageWithLoading { findNavController().navigate(R.id.navigation_feed) }
        }

        // Monitor
        view.findViewById<MaterialCardView>(R.id.menu_monitor)?.setOnClickListener {
            navigateToPageWithLoading { findNavController().navigate(R.id.navigation_monitor) }
        }

        // Analytics
        view.findViewById<MaterialCardView>(R.id.menu_analytics)?.setOnClickListener {
            navigateToPageWithLoading { findNavController().navigate(R.id.navigation_analytics) }
        }

        // Settings
        view.findViewById<MaterialCardView>(R.id.menu_settings)?.setOnClickListener {
            navigateToPageWithLoading { findNavController().navigate(R.id.navigation_settings) }
        }
    }

    private fun navigateToPageWithLoading(navigationAction: () -> Unit) {
        val loading = LoadingUtils.showLoading(requireContext())
        Handler(Looper.getMainLooper()).postDelayed({
            loading.dismiss()
            navigationAction()
        }, 1200) // 1.2 seconds loading for smooth feel
    }
}
