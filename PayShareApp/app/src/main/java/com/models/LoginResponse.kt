package com.models

data class LoginResponse(
    val token: String,
    val email: String,
    val id: Number,
    val name: String
)