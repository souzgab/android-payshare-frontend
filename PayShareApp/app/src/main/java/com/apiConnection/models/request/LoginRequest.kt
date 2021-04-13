package com.apiConnection.models.request

import com.apiConnection.models.dataClassAdapter.LoginData
import com.apiConnection.models.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginRequest {
    @Headers("Content-Type: application/json")
    @POST(value = "v1/payshare/auth/login")
    fun postLogin(@Body User: LoginData): Call<LoginResponse>

}