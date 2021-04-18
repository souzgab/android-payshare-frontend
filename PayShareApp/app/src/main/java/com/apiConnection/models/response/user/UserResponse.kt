package com.apiConnection.models.response.user

import com.apiConnection.dataClassAdapter.lobby.LobbyUserHistoryData
import com.apiConnection.dataClassAdapter.transactions.TransactionData
import com.apiConnection.dataClassAdapter.transactions.TransactionWalletData

data class UserResponse (
    val userId: Number,
    val name: String,
    val age: Number,
    val address: String,
    val city: String,
    val cep: String,
    val state: String,
    val email: String,
    val password: String,
    val cpf: String,
    val rg: String,
    val userAmount: Double,
    val userLobbyHost: Boolean,
    val userAmountLobby: Double?,
    val transactions: List<TransactionData>,
    val transactionWallets: List<TransactionWalletData>,
    val lobbyUserList: List<LobbyUserHistoryData>,
    val roles: List<Any?>
)