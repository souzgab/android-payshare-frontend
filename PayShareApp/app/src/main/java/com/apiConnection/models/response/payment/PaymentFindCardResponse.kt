package com.apiConnection.models.response.payment

data class PaymentFindCardResponse (
    val id: String,
    val cvv: String,
    val cardNumber: String,
    val cardNumberFourStart: String,
    val cardNumberFourEnd: String,
    val expiryDate: String,
    val type: String,
    val cardName: String,
    val idUsuario: Number,
    val createdAt: String,
    val updatedAt: String
)
