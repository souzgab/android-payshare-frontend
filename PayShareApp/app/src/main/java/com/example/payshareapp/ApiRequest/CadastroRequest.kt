package com.example.payshareapp.ApiRequest

import com.example.payshareapp.ApiResponse.CadastroResponse
import retrofit2.Call
import retrofit2.http.POST

interface CadastroRequest {
    @POST(value = "auth/signup")
    fun postCadastro(): Call<CadastroResponse>
}