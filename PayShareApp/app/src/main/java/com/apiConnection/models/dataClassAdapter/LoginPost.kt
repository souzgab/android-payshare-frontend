package com.apiConnection.models.dataClassAdapter

import com.google.gson.annotations.SerializedName

data class LoginPost(
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String
)