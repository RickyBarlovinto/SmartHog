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
import androidx.fragment.app.viewModels
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var updateTimeRunnable: Runnable

    private val monitorViewModel: MonitorViewModel by viewModels()

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

        val tvTotalPigs = view.findViewById<TextView>(R.id.tv_total_pigs_dashboard)
        val tvAvgWeight = view.findViewById<TextView>(R.id.tv_avg_weight_dashboard)
        val tvActiveSchedules = view.findViewById<TextView>(R.id.tv_active_schedules_dashboard)
        val tvHealthAlerts = view.findViewById<TextView>(R.id.tv_health_alerts_dashboard)

        loadUserData(view)
        startDateTimeUpdates()

        view.findViewById<View>(R.id.dashboard_profile_icon)?.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        // Dynamic Data Observation
        monitorViewModel.pigList.observe(viewLifecycleOwner) { _ ->
            updateDashboardUI(view)
        }

        monitorViewModel.batchOverview.observe(viewLifecycleOwner) {
            updateDashboardUI(view)
        }

        monitorViewModel.loadData(requireContext())
        monitorViewModel.fetchApiData()
    }

    private fun updateDashboardUI(view: View) {
        val tvTotalPigs = view.findViewById<TextView>(R.id.tv_total_pigs_dashboard)
        val tvAvgWeight = view.findViewById<TextView>(R.id.tv_avg_weight_dashboard)
        val tvActiveSchedules = view.findViewById<TextView>(R.id.tv_active_schedules_dashboard)
        val tvHealthAlerts = view.findViewById<TextView>(R.id.tv_health_alerts_dashboard)

        val overview = monitorViewModel.batchOverview.value
        val pigs = monitorViewModel.pigList.value ?: emptyList()

        if (overview != null) {
            tvTotalPigs.text = overview.totalPigs.toString()
            tvAvgWeight.text = String.format(Locale.US, "%.1f", overview.avgWeightToday)
            // Keep status update from local/api batches if needed
        } else {
            tvTotalPigs.text = pigs.size.toString()
            val totalWeight = pigs.sumOf { it.weight }
            val avgWeight = if (pigs.isNotEmpty()) totalWeight / pigs.size else 0.0
            tvAvgWeight.text = String.format(Locale.US, "%.1f", avgWeight)
        }

        val alerts = pigs.count { it.status == "Below" }
        tvHealthAlerts.text = "$alerts Due"

        // Fetching Schedules count
        RetrofitClient.instance.getSchedules().enqueue(object : retrofit2.Callback<List<FeedingSchedule>> {
            override fun onResponse(call: retrofit2.Call<List<FeedingSchedule>>, response: retrofit2.Response<List<FeedingSchedule>>) {
                if (response.isSuccessful) {
                    val count = response.body()?.size ?: 0
                    tvActiveSchedules.text = "$count Schedules"
                }
            }
            override fun onFailure(call: retrofit2.Call<List<FeedingSchedule>>, t: Throwable) {}
        })
    }

    private fun startDateTimeUpdates() {
        updateTimeRunnable = object : Runnable {
            override fun run() {
                if (!isAdded) return
                val now = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

                tvDate.text = dateFormat.format(now)
                tvTime.text = timeFormat.format(now)

                handler.postDelayed(this, 1000)
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

        // Get current hour to determine time-based greeting
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val timeGreeting = when (hour) {
            in 0..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..20 -> "Good evening"
            else -> "Good night"
        }

        // Check if user was remembered to personalize the welcome
        val loginPrefs = requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val isRemembered = loginPrefs.getBoolean("remember_device", false)
        
        val finalGreeting = if (isRemembered) {
            "Welcome back! $timeGreeting, $name"
        } else {
            "$timeGreeting, $name"
        }

        welcomeText?.text = finalGreeting

        if (imageString != null && profileIcon != null) {
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
        handler.removeCallbacks(updateTimeRunnable) 
        super.onDestroyView() // FIXED: Correct super call
    }
}
