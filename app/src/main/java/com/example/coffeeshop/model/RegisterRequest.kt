package com.example.coffeeshop.model


data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val msg: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val msg: String,
    val token: String,
    val user: UserData
)

data class UserData(
    val id: String,
    val username: String,
    val email: String
)

data class UpdateProfileRequest(
    val userId: String,
    val username: String,
    val email: String
)

data class ChangePasswordRequest(
    val email: String,
    val currentPassword: String,
    val newPassword: String
)

data class ApiResponse(
    val msg: String
)
