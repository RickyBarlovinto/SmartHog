package com.example.smart_hog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // I-inflate ang layout na may custom graph placeholder
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Dito mo pwedeng ilagay ang iba pang logic sa hinaharap.
        // Sa ngayon, ang graph ay naka-define na sa XML gamit ang mga simple Views.
    }
}
