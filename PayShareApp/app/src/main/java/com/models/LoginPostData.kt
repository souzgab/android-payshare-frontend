package com.models

import com.google.gson.annotations.SerializedName

data class LoginPostData(
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String
)