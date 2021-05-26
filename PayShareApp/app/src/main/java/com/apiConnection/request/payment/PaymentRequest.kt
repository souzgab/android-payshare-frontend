package com.apiConnection.request.payment

import com.apiConnection.dataClassAdapter.lobby.LobbyDataBody
import com.apiConnection.dataClassAdapter.payments.PaymentFindCard
import com.apiConnection.models.response.lobby.LobbyResponse
import com.apiConnection.models.response.payment.PaymentFindCardByIdResponse
import com.apiConnection.models.response.payment.PaymentFindCardResponse
import retrofit2.Call
import retrofit2.http.*

interface PaymentRequest {
    @Headers("Content-Type: application/json")
    @POST(value = "card/find")
    fun findCard(@Body card: PaymentFindCard): Call<PaymentFindCardResponse>

    @Headers("Content-Type: application/json")
    @GET(value = "card/{idUser}")
    fun findCardByUserId(@Path("idUser") id: Int): Call<PaymentFindCardByIdResponse>
}