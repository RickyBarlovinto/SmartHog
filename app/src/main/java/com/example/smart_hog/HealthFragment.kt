package com.example.smart_hog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HealthFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_health, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back Button Functionality
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back_health)
        btnBack?.setOnClickListener {
            findNavController().navigate(R.id.navigation_menu)
        }

        recyclerView = view.findViewById(R.id.pigRecyclerView)
        val pigList = listOf(
            Pig("#PG-1024", "Batch: B-2024-MAR", "85 kg", "Ahead"),
            Pig("#PG-1025", "Batch: B-2024-MAR", "82 kg", "Normal"),
            Pig("#PG-1026", "Batch: B-2024-MAR", "80 kg", "Below"),
            Pig("#PG-1027", "Batch: B-2024-MAR", "90 kg", "Ahead")
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = PigAdapter(pigList)
    }
}
