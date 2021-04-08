package com.example.payshareapp

import com.example.payshareapp.ApiRequest.CadastroRequest
import com.example.payshareapp.ApiRequest.LoginRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Conexao {
    private const val backendPath: String = "http://3.211.150.177:8080/"
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