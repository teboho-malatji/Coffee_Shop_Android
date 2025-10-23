package com.example.coffeeshop.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coffeeshop.api.RetrofitClient
import com.example.coffeeshop.databinding.ActivitySignupBinding
import com.example.coffeeshop.model.RegisterRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class SignupActivity : AppCompatActivity() {

    // ViewBinding instance for signup_activity.xml
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle "Sign Up" button click
        binding.signupBtn.setOnClickListener {
            val username = binding.Username.text.toString().trim()
            val email = binding.Email.text.toString().trim()
            val password = binding.Password.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call API to register user
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.api.register(
                        RegisterRequest(username, email, password)
                    )
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(
                            this@SignupActivity,
                            response.body()!!.msg,
                            Toast.LENGTH_SHORT
                        ).show()
                        // Navigate to LoginActivity after successful signup
                        startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            "Signup failed: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: IOException) {
                    Toast.makeText(this@SignupActivity, "Network Error", Toast.LENGTH_SHORT).show()
                } catch (e: HttpException) {
                    Toast.makeText(this@SignupActivity, "HTTP Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Navigate to LoginActivity if user already has an account
        binding.LoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
