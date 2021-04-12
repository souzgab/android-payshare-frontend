package com.apiConnection.models.response

data class LoginResponse(
    val token: String,
    val email: String,
    val id: Number,
    val name: String
)