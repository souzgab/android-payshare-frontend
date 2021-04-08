package com.example.payshareapp.ApiResponse

data class CadastroResponse(
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
    val transactions: List<Any?>,
    val transactionWallets: List<Any?>,
    val lobbyUserList: List<Any?>,
    val roles: List<Any?>
)
