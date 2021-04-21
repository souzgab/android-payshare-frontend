package com.apiConnection.models.response.transaction

import java.math.BigDecimal

data class TransactionWalletResponse(
    val headers : Any?,
    val body : TransactionWalletBody,
    val statusCode : String,
    val statusCodeValue : Int
)