package com.apiConnection.models.response.transaction

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class TransactionResponse (
   val transactionDate : Int,
   val amount : Double,
   val status : String,
   val description : String,
   val paymentMethod : String,
   val currencyId: String,
   val externalReference : String,
   val createdAt : String,
   val expirationDate : String,
   val initPoint : String,
   val cupomUser: String
)