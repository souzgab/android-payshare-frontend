package com.apiConnection.dataClassAdapter.lobby

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class LobbyDataBody (
    @SerializedName("lobbyDescription") var lobbyDescription : String,
    @SerializedName("orderDescription") var orderDescription : String,
    @SerializedName("amount") var amount : Double,
    @SerializedName("amountTotal") var amountTotal : Double
)