package com.apiConnection.request.transactions

import com.apiConnection.models.response.transaction.TransactionResponse
import com.apiConnection.models.response.transaction.TransactionWalletResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface TransactionRequest {
    @Headers("Content-Type: application/json")
    @POST(value = "v1/payshare/transaction/wallet/{idUser}/{amount}")
    fun addMoneyInWallet(
        @Path("idUser") id: Int,
        @Path("amount") amount: Double,
        @Header("Authorization") token: String
    ): Call<TransactionWalletResponse>

    @Headers("Content-Type: application/json")
    @POST(value = "v1/payshare/transaction/wallet-lobby/{idUser}/{amount}")
    fun paymentWalletLobby(
        @Path("idUser") id: Int,
        @Path("amount") amount: Double,
        @Header("Authorization") token: String
    ): Call<TransactionResponse>

    @Headers("Content-Type: application/json")
    @POST(value = "v1/payshare/transaction/wallet-transfer/{idUser}/{amount}/{cpfDocument}")
    fun transferAccounts(
        @Path("idUser") id: Int,
        @Path("amount") amount: Double,
        @Path("cpfDocument") cpfDocument: String,
        @Header("Authorization") token: String
    ): Call<TransactionResponse>
}