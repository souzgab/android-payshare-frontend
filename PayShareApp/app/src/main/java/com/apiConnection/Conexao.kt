package com.apiConnection

import com.apiConnection.dataClassAdapter.transactions.TransactionData
import com.apiConnection.request.lobby.LobbyRequest
import com.apiConnection.request.payment.PaymentRequest
import com.apiConnection.request.transactions.TransactionRequest
import com.apiConnection.request.user.CadastroRequest
import com.apiConnection.request.user.LoginRequest
import com.apiConnection.request.user.UserRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Conexao {

   //private const val backendPath: String = "http://10.0.2.2:8080/"
    private const val backendPath: String = "http://34.194.50.29/"
    private const val walletPath: String = "https://wallet-payshare.herokuapp.com"
    //private const val backendPath: String = "https://paysharedev.herokuapp.com/"

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


    fun findUserById() : UserRequest {
        return Retrofit.Builder()
            .baseUrl(backendPath)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserRequest::class.java)
    }

    //// Lobby

    fun createLobby() : LobbyRequest{
        return Retrofit.Builder()
            .baseUrl(backendPath)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LobbyRequest::class.java)
    }

    fun findByLobbyUser() : LobbyRequest{
        return Retrofit.Builder()
            .baseUrl(backendPath)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LobbyRequest::class.java)
    }


    // Transaction

    fun createTransactionWallet() : TransactionRequest{
        return Retrofit.Builder()
            .baseUrl(backendPath)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TransactionRequest::class.java)
    }

    //
    fun wallet(): PaymentRequest {
        return Retrofit.Builder()
            .baseUrl(walletPath)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PaymentRequest::class.java)
    }
}
