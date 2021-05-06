package com.apiConnection.request.lobby

import com.apiConnection.dataClassAdapter.lobby.LobbyData
import com.apiConnection.dataClassAdapter.lobby.LobbyDataBody
import com.apiConnection.dataClassAdapter.user.LoginData
import com.apiConnection.models.response.lobby.LobbyResponse
import com.apiConnection.models.response.user.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface LobbyRequest {

    @Headers("Content-Type: application/json")
    @GET(value = "v1/payshare/lobby/lobbyUser/{id}")
    fun findLobbyUser(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<LobbyResponse>

    @Headers("Content-Type: application/json")
    @GET(value = "v1/payshare/lobby/deleteLobby/{id}")
    fun deleteLobby(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<Void>

    @Headers("Content-Type: application/json")
    @POST(value = "v1/payshare/lobby/saveLobby/{id}")
    fun createLobby(
        @Body lobby: LobbyDataBody,
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<LobbyResponse>
}