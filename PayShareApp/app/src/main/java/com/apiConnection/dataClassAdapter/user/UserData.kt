package com.apiConnection.dataClassAdapter.user

import com.apiConnection.dataClassAdapter.lobby.LobbyUserHistoryData
import com.apiConnection.dataClassAdapter.transactions.TransactionData
import com.apiConnection.dataClassAdapter.transactions.TransactionWalletData
import com.google.gson.annotations.SerializedName

data class UserData (
    @SerializedName("userId")val userId: Number,
    @SerializedName("name")val name: String,
    @SerializedName("age")val age: Number,
    @SerializedName("address")val address: String,
    @SerializedName("city")val city: String,
    @SerializedName("cep")val cep: String,
    @SerializedName("state")val state: String,
    @SerializedName("email")val email: String,
    @SerializedName("password")val password: String,
    @SerializedName("cpf")val cpf: String,
    @SerializedName("rg")val rg: String,
    @SerializedName("userAmount")val userAmount: Double,
    @SerializedName("userLobbyHost")val userLobbyHost: Boolean,
    @SerializedName("userAmountLobby")val userAmountLobby: Double?,
    @SerializedName("transactions")val transactions: List<TransactionData>,
    @SerializedName("transactionWallets")val transactionWallets: List<TransactionWalletData>,
    @SerializedName("lobbyUserList")val lobbyUserList: List<LobbyUserHistoryData>,
    @SerializedName("roles")val roles: List<Any?>
)