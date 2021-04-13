package com.apiConnection

import com.apiConnection.models.request.CadastroRequest
import com.apiConnection.models.request.LoginRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Conexao {
    private const val backendPath: String = "http://payshare.ddns.net/"

    fun loginApi() : LoginRequest {
        return Retrofit.Builder()
            .baseUrl(backendPath)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginRequest::class.java)
    }

    fun cadastroApi() : CadastroRequest {
        return Retrofit.Builder()
            .baseUrl(backendPath)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CadastroRequest::class.java)
    }

}