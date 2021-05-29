package com.apiConnection.dataClassAdapter.payments

import com.google.gson.annotations.SerializedName

data class PaymentPostCard(
    @SerializedName("idUsuario") var idUsuario: Number,
    @SerializedName("cvv") var cvv: String,
    @SerializedName("cardNumber") var cardNumber: String,
    @SerializedName("expiryDate") var expiryDate: String,
    @SerializedName("cardName") var cardName: String,
    @SerializedName("type") var credito: String
)
