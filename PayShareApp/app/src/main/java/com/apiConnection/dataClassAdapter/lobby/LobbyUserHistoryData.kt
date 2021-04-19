package com.apiConnection.dataClassAdapter.lobby

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class LobbyUserHistoryData(
    @SerializedName("id") var id : Int,
    @SerializedName("lobbyDescription") var lobbyDescription : String,
    @SerializedName("orderDescription") var orderDescription : String,
    @SerializedName("amount") var amount : BigDecimal,
    @SerializedName("amountTotal") var amountTotal : BigDecimal,
    @SerializedName("creationDate") var creationDate : String,
    @SerializedName("expirationDate") var expirationDate : String
)