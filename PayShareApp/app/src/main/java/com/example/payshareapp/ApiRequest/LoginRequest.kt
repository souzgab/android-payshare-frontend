package com.example.payshareapp.ApiRequest

import com.example.payshareapp.ApiResponse.LoginResponse
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginRequest {
    @Headers("Content-Type: application/json")
    @POST(value = "v1/payshare/auth/login")
    fun postLogin(@Body User: LoginPostData ): Call<LoginResponse>

}

data class LoginPostData(
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String
)




