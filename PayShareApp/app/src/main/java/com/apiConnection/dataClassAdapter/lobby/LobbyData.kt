package com.apiConnection.dataClassAdapter.lobby

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class LobbyData(
    @SerializedName("id") var id : Int,
    @SerializedName("lobbyDescription") var lobbyDescription : String,
    @SerializedName("orderDescription") var orderDescription : String,
    @SerializedName("amount") var amount : Double,
    @SerializedName("amountTotal") var amountTotal : Double,
    @SerializedName("creationDate") var creationDate : String,
    @SerializedName("expirationDate") var expirationDate : String,
    @SerializedName("lobbyOpen") var lobbyOpen : Boolean
)