package com.example.coffeeshop.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coffeeshop.api.RetrofitClient
import com.example.coffeeshop.databinding.ActivityProfileBinding
import com.example.coffeeshop.model.ChangePasswordRequest
import com.example.coffeeshop.model.UpdateProfileRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPref = getSharedPreferences("CoffeeShopPrefs", MODE_PRIVATE)

        setupUserInfo()
        setupClickListeners()
    }

    private fun setupUserInfo() {
        // Load current user data from SharedPreferences
        val username = sharedPref.getString("USER_NAME", "") ?: ""
        val email = sharedPref.getString("USER_EMAIL", "") ?: ""
        val userId = sharedPref.getString("USER_ID", "") ?: ""

        binding.etUsername.setText(username)
        binding.etEmail.setText(email)
        binding.etUserId.setText(userId)

        // User ID is not editable
        binding.etUserId.isEnabled = false
    }

    private fun setupClickListeners() {
        // Update Profile Button
        binding.btnUpdateProfile.setOnClickListener {
            updateProfile()
        }

        // Change Password Button
        binding.btnChangePassword.setOnClickListener {
            changePassword()
        }

        // Logout Button
        binding.btnLogout.setOnClickListener {
            logout()
        }

        // Back Button
        binding.btnBack.setOnClickListener {
            finish() // Simply go back to previous activity
        }
    }

    private fun updateProfile() {
        val username = binding.etUsername.text?.toString()?.trim() ?: ""
        val email = binding.etEmail.text?.toString()?.trim() ?: ""
        val userId = binding.etUserId.text?.toString()?.trim() ?: ""

        if (username.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val token = sharedPref.getString("JWT_TOKEN", "") ?: ""

                val response = RetrofitClient.api.updateProfile(
                    authorization = "Bearer $token",
                    request = UpdateProfileRequest(userId, username, email)
                )

                if (response.isSuccessful && response.body() != null) {
                    // Update local storage
                    with(sharedPref.edit()) {
                        putString("USER_NAME", username)
                        putString("USER_EMAIL", email)
                        apply()
                    }

                    Toast.makeText(this@ProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@ProfileActivity, "Failed to update profile: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this@ProfileActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Toast.makeText(this@ProfileActivity, "Server error: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changePassword() {
        val currentPassword = binding.etCurrentPassword.text?.toString()?.trim() ?: ""
        val newPassword = binding.etNewPassword.text?.toString()?.trim() ?: ""
        val confirmPassword = binding.etConfirmPassword.text?.toString()?.trim() ?: ""

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all password fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(this, "New passwords don't match", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val token = sharedPref.getString("JWT_TOKEN", "") ?: ""
                val email = sharedPref.getString("USER_EMAIL", "") ?: ""

                val response = RetrofitClient.api.changePassword(
                    authorization = "Bearer $token",
                    request = ChangePasswordRequest(email, currentPassword, newPassword)
                )

                if (response.isSuccessful && response.body() != null) {
                    // Clear password fields with proper null safety
                    binding.etCurrentPassword.text?.clear()
                    binding.etNewPassword.text?.clear()
                    binding.etConfirmPassword.text?.clear()

                    Toast.makeText(this@ProfileActivity, "Password changed successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@ProfileActivity, "Failed to change password: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Toast.makeText(this@ProfileActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Toast.makeText(this@ProfileActivity, "Server error: ${e.message}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logout() {
        // Clear all user data from SharedPreferences
        with(sharedPref.edit()) {
            clear()
            apply()
        }

        // Navigate to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}