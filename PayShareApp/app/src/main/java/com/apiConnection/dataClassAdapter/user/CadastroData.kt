package com.apiConnection.dataClassAdapter.user

import com.google.gson.annotations.SerializedName

data class CadastroData(
    @SerializedName("name") var name: String,
    @SerializedName("age") var age: Number,
    @SerializedName("address") var address: String,
    @SerializedName("city") var city: String,
    @SerializedName("cep") var cep: String,
    @SerializedName("state") var state: String,
    @SerializedName("email") var email: String,
    @SerializedName("password") var password: String,
    @SerializedName("cpf") var cpf: String,
    @SerializedName("rg") var rg: String
)
