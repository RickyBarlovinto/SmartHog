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

        loadUserData(view)

        // Profile Icon Click
        view.findViewById<View>(R.id.profile_icon).setOnClickListener {
            navigateToPageWithLoading {
                val intent = Intent(requireContext(), ProfileActivity::class.java)
                startActivity(intent)
            }
        }

        // Navigation Items with Professional Loading Indicator
        view.findViewById<MaterialCardView>(R.id.menu_dashboard).setOnClickListener {
            navigateToPageWithLoading { findNavController().navigate(R.id.navigation_dashboard) }
        }

        view.findViewById<MaterialCardView>(R.id.menu_feed).setOnClickListener {
            navigateToPageWithLoading { findNavController().navigate(R.id.navigation_feed) }
        }

        view.findViewById<MaterialCardView>(R.id.menu_health).setOnClickListener {
            navigateToPageWithLoading { findNavController().navigate(R.id.navigation_health) }
        }

        view.findViewById<MaterialCardView>(R.id.menu_analytics).setOnClickListener {
            navigateToPageWithLoading { findNavController().navigate(R.id.navigation_analytics) }
        }

        view.findViewById<MaterialCardView>(R.id.menu_settings).setOnClickListener {
            navigateToPageWithLoading { findNavController().navigate(R.id.navigation_settings) }
        }
    }

    private fun navigateToPageWithLoading(navigationAction: () -> Unit) {
        val loading = LoadingUtils.showLoading(requireContext())
        Handler(Looper.getMainLooper()).postDelayed({
            loading.dismiss()
            navigationAction()
        }, 1500) // 1.5 seconds loading for smooth feel
    }

    private fun loadUserData(view: View) {
        val prefs = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        val imageString = prefs.getString("image", null)
        val profileIcon = view.findViewById<ImageView>(R.id.profile_icon)

        if (imageString != null) {
            val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            profileIcon.setImageBitmap(bitmap)
        }
    }

    override fun onResume() {
        super.onResume()
        view?.let { loadUserData(it) }
    }
}
