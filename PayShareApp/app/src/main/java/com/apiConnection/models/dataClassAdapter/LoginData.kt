package com.apiConnection.models.dataClassAdapter

import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String
)