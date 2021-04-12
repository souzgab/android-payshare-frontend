package com.connection

import com.resttemplate.CadastroRequest
import com.resttemplate.LoginRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Conexao {
    private const val backendPath: String = "https://paysharedev.herokuapp.com/"
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