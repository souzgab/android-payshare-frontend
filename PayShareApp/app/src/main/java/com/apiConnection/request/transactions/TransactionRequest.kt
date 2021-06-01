package com.apiConnection.request.transactions

import com.apiConnection.models.response.transaction.TransactionResponse
import com.apiConnection.models.response.transaction.TransactionWalletResponse
import retrofit2.Call
import retrofit2.http.*

interface TransactionRequest {
    @Headers("Content-Type: application/json")
    @POST(value = "v1/payshare/transaction/wallet/{idUser}/{amount}")
    fun addMoneyInWallet(
        @Path("idUser") id: Int,
        @Path("amount") amount: Double,
        @Header("Authorization") token: String
    ): Call<TransactionWalletResponse>

    @Headers("Content-Type: application/json")
    @POST(value = "v1/payshare/transaction/{idUser}/{amount}")
    fun createTransactionLobbyMercadoPago(
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

    @Headers("Content-Type: application/json")
    @POST(value = "v1/payshare/transaction/card-lobby/{idUser}/{amount}")
    fun paymentCard (
        @Path("idUser") id: Int,
        @Path("amount") amount: Double,
        @Header("Authorization") token: String
    ): Call<TransactionResponse>
}