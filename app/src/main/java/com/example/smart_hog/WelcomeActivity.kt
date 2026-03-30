package com.example.smart_hog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val btnGetStarted = findViewById<Button>(R.id.btn_get_started)
        btnGetStarted.setOnClickListener {
            val loading = LoadingUtils.showLoading(this)
            
            // Simula ng Loading Delay (2 seconds) para makita ang cool logo animation
            Handler(Looper.getMainLooper()).postDelayed({
                loading.dismiss()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }
    }
}
