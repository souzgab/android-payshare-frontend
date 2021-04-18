package com.apiConnection.request.user

import com.apiConnection.models.response.user.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserRequest {

    @Headers("Content-Type: application/json")
    @GET(value = "v1/payshare/user/{id}")
    fun findUserById(@Path ("id") id : Int , @Header ("Authorization") token : String): Call<UserResponse>
}