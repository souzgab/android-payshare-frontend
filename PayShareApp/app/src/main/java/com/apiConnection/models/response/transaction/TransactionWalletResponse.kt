package com.apiConnection.models.response.transaction

import java.math.BigDecimal

data class TransactionWalletResponse(
    val transactionDate : Int,
    val amount : BigDecimal,
    val status : String,
    val description : String,
    val paymentMethod : String,
    val currencyId: String,
    val externalReference : String,
    val createdAt : String,
    val expirationDate : String,
    val initPoint : String
)