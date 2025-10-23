package com.example.coffeeshop.api

import com.example.coffeeshop.model.ApiResponse
import com.example.coffeeshop.model.ChangePasswordRequest
import com.example.coffeeshop.model.LoginRequest
import com.example.coffeeshop.model.LoginResponse
import com.example.coffeeshop.model.RegisterRequest
import com.example.coffeeshop.model.RegisterResponse
import com.example.coffeeshop.model.UpdateProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


    @PUT("/api/auth/profile")
    suspend fun updateProfile(
        @Header("Authorization") authorization: String,
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse>

    @PUT("/api/auth/change-password")
    suspend fun changePassword(
        @Header("Authorization") authorization: String,
        @Body request: ChangePasswordRequest
    ): Response<ApiResponse>
}