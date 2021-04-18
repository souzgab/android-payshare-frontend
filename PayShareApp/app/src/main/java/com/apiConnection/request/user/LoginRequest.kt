package com.apiConnection.request.user

import com.apiConnection.dataClassAdapter.user.LoginData
import com.apiConnection.models.response.user.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginRequest {
    @Headers("Content-Type: application/json")
    @POST(value = "v1/payshare/auth/login")
    fun postLogin(@Body User: LoginData): Call<LoginResponse>

}