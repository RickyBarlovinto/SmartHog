package com.example.smart_hog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        // Navigation for Dashboard
        view.findViewById<MaterialCardView>(R.id.menu_dashboard).setOnClickListener {
            findNavController().navigate(R.id.navigation_dashboard)
        }

        // Navigation for Feed
        view.findViewById<MaterialCardView>(R.id.menu_feed).setOnClickListener {
            findNavController().navigate(R.id.navigation_feed)
        }

        // Navigation for Health
        view.findViewById<MaterialCardView>(R.id.menu_health).setOnClickListener {
            findNavController().navigate(R.id.navigation_health)
        }

        // Navigation for Analytics
        view.findViewById<MaterialCardView>(R.id.menu_analytics).setOnClickListener {
            findNavController().navigate(R.id.navigation_analytics)
        }
    }
}
