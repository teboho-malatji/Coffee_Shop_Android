package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeeshop.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if user is already logged in
        val sharedPref = getSharedPreferences("CoffeeShopPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("JWT_TOKEN", null)

        if (!token.isNullOrEmpty()) {
            // Already logged in → go to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            // User not logged in → show Get Started button
            binding.startBtn.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}
