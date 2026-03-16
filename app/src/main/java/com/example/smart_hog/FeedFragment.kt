package com.example.smart_hog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back Button Functionality - Babalik sa Menu
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back_feed)
        btnBack?.setOnClickListener {
            findNavController().navigate(R.id.navigation_menu)
        }

        // Calendar Logic
        val calendar = view.findViewById<CalendarView>(R.id.sellCalendar)
        calendar?.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(requireContext(), "Selling schedule: $selectedDate", Toast.LENGTH_SHORT).show()
        }
    }
}
