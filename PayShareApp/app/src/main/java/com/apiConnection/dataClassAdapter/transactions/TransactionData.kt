package com.apiConnection.dataClassAdapter.transactions

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class TransactionData (
    @SerializedName("transactionId") var transactionDate : Int,
    @SerializedName("amount") var amount : Double,
    @SerializedName("status") var status : String,
    @SerializedName("description") var description : String,
    @SerializedName("paymentMethod") var paymentMethod : String,
    @SerializedName("currencyId") var currencyId: String,
    @SerializedName("externalReference") var externalReference : String,
    @SerializedName("createdAt") var createdAt : String,
    @SerializedName("expirationDate") var expirationDate : String,
    @SerializedName("initPoint") var initPoint : String,
    @SerializedName("cupomUser") var cupomUser: String

)