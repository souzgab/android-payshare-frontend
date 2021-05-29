package com.apiConnection.dataClassAdapter.payments

data class Card (
    var cvv: String,
    var cardNumber: String,
    var expiryDate: String,
    var cardName: String,
    var idUsuario: Any,
    var type: String
)