package com.apiConnection.dataClassAdapter.payments

import com.google.gson.annotations.SerializedName

data class PaymentFindCard(
    @SerializedName("id") var cardNumber: String,
    @SerializedName("cvv") var cvv: String
)
