package com.apiConnection.request.user

import com.apiConnection.dataClassAdapter.user.CadastroData
import com.apiConnection.models.response.user.CadastroResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CadastroRequest {
    @Headers("Content-Type: application/json")
    @POST(value = "v1/payshare/auth/signup")
    fun postCadastro(@Body User: CadastroData): Call<CadastroResponse>
}