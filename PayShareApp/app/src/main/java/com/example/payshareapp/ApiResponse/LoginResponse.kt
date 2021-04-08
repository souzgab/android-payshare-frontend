package com.example.payshareapp.ApiResponse

data class LoginResponse(
    val token: String,
    val email: String,
    val id: Number,
    val name: String
)
