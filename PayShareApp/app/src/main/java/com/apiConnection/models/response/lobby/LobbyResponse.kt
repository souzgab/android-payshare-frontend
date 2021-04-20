package com.apiConnection.models.response.lobby

import com.apiConnection.dataClassAdapter.transactions.TransactionData
import com.apiConnection.dataClassAdapter.user.UserData
import com.apiConnection.models.response.user.UserResponse
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class LobbyResponse (
    var id : Int,
    var lobbyDescription : String,
    var orderDescription : String,
    var amount : BigDecimal,
    var amountTotal : BigDecimal,
    var creationDate : String,
    var expirationDate : String,
    var lobbyOpen : Boolean,
    var userPfList : List<UserData>,
    var transactions : List<TransactionData>
)