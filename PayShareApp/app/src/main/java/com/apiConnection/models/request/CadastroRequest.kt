package com.apiConnection.models.request

import com.apiConnection.models.response.CadastroResponse
import retrofit2.Call
import retrofit2.http.POST

interface CadastroRequest {
    @POST(value = "auth/signup")
    fun postCadastro(): Call<CadastroResponse>
}