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
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateTimeRunnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        tvDate = view.findViewById(R.id.tv_current_date)
        tvTime = view.findViewById(R.id.tv_current_time)

        loadUserData(view)
        startDateTimeUpdates()

        // Navigate to Profile
        view.findViewById<View>(R.id.dashboard_profile_icon)?.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startDateTimeUpdates() {
        updateTimeRunnable = object : Runnable {
            override fun run() {
                val now = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

                tvDate.text = dateFormat.format(now)
                tvTime.text = timeFormat.format(now)

                handler.postDelayed(this, 1000) // Update every second
            }
        }
        handler.post(updateTimeRunnable)
    }

    private fun loadUserData(view: View) {
        val prefs = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        val name = prefs.getString("name", "Silas")
        val imageString = prefs.getString("image", null)

        val welcomeText = view.findViewById<TextView>(R.id.welcome_text)
        val profileIcon = view.findViewById<ImageView>(R.id.dashboard_profile_icon)

        welcomeText.text = "Good morning, $name"

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

    override fun onDestroyView() {
        super.onResume()
        handler.removeCallbacks(updateTimeRunnable) // Stop updates when fragment destroyed
    }
}
