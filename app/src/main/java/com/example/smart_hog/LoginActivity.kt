package com.example.smart_hog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEt: TextInputEditText
    private lateinit var passwordEt: TextInputEditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEt = findViewById(R.id.emailEt)
        passwordEt = findViewById(R.id.passwordEt)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = emailEt.text.toString()
            val password = passwordEt.text.toString()

            if (validateInput(email, password)) {
                // Show Professional Loading Indicator
                val loading = LoadingUtils.showLoading(this)
                
                Handler(Looper.getMainLooper()).postDelayed({
                    loading.dismiss()
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 2000) // 2 seconds delay to show the logo rotation
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            emailEt.error = "Email cannot be empty"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEt.error = "Invalid email address"
            return false
        }
        if (password.isEmpty()) {
            passwordEt.error = "Password cannot be empty"
            return false
        }
        if (password.length < 6) {
            passwordEt.error = "Password must be at least 6 characters long"
            return false
        }
        return true
    }
}
