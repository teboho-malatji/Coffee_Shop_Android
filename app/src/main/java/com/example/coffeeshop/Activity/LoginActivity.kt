package com.example.coffeeshop.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coffeeshop.api.RetrofitClient
import com.example.coffeeshop.databinding.ActivityLoginBinding
import com.example.coffeeshop.model.LoginRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPref = getSharedPreferences("CoffeeShopPrefs", MODE_PRIVATE)

        binding.loginBtn.setOnClickListener {
            val email = binding.EmailLogin.text.toString().trim()
            val password = binding.PasswordLogin.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.api.login(LoginRequest(email, password))
                    if (response.isSuccessful && response.body() != null) {
                        val loginResponse = response.body()!!

                        // ✅ SAVE THE TOKEN TO SHAREDPREFERENCES
                        with(sharedPref.edit()) {
                            putString("JWT_TOKEN", loginResponse.token)
                            putString("USER_ID", loginResponse.user.id)
                            putString("USER_EMAIL", loginResponse.user.email)
                            putString("USER_NAME", loginResponse.user.username)
                            apply()
                        }

                        Toast.makeText(
                            this@LoginActivity,
                            "Welcome ${loginResponse.user.username}!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate to MainActivity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()

                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: IOException) {
                    Toast.makeText(this@LoginActivity, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
                } catch (e: HttpException) {
                    Toast.makeText(this@LoginActivity, "HTTP Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.SignupLink.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}