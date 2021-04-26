package com.payshare.payshareapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import com.apiConnection.Conexao
import com.apiConnection.models.response.lobby.LobbyResponse
import com.apiConnection.models.response.user.UserResponse
import com.google.gson.Gson
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Sala_PagamentoFragment : Fragment() {

    private val findByLobbyUser = Conexao.findByLobbyUser()
    private val findUserById = Conexao.findUserById()
    private val transaction = Conexao.createTransactionWallet()
    lateinit var preferencias: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_sala_pagamento, container, false)
        //============= recuperando dados amarzenado em cache ========================
        preferencias =
            this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val idUser = preferencias.getString("idUser", null)
        val token = preferencias.getString("Auth", null)
        var nameUser = preferencias.getString("nameUser", null)
        var moneyShared = preferencias.getFloat("userAmount", 0.00F)
        var idLobby = preferencias.getString("idLobby", null)

        // ============================================================================
        val btnPagar: Button = view.findViewById(R.id.btn_pagar_lobby)

        //========= Busca informações da lobby do usuario ======================================
        idUser?.toInt()?.let {
            findByLobbyUser.findLobbyUser(it, "Bearer " + token.toString())
                .enqueue(object : Callback<LobbyResponse> {
                    override fun onResponse(
                        call: Call<LobbyResponse>,
                        response: Response<LobbyResponse>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            val editor = preferencias.edit()
                            val valorTotalPagar: TextView = view.findViewById(R.id.valor_total)
                            val valorTotalRecebido: TextView =
                                view.findViewById(R.id.valor_total_recebido)
                            Log.e("Sucesso", "Lobby" + Gson().toJson(data))
                            valorTotalPagar.text = "R$ ${data.amount}"
                            valorTotalRecebido.text = "R$ ${data.amountTotal}"
                            if (data.amount == data.amountTotal){
                                val btnFinalizar : Button = view.findViewById(R.id.btn_finalizar)
                                btnFinalizar.visibility = View.VISIBLE
                                btnPagar.visibility = View.INVISIBLE
                            }
                        }
                    }

                    override fun onFailure(call: Call<LobbyResponse>, t: Throwable) {
                        Log.e("Erro", "erro " + t.message)
                    }
                })
        }

        // ======================= PAGAMENTO LOBBY =========================


//        btnPagar.setOnClickListener {
//
//            idUser?.toInt()?.let {
//                findUserById.findUserById(it, "Bearer " + token.toString())
//                    .enqueue(object : Callback<UserResponse> {
//                        override fun onResponse(
//                            call: Call<UserResponse>,
//                            response: Response<UserResponse>
//                        ) {
//                            var data = response.body()
//
//                            if (data != null) {
//                                idUser?.toInt()?.let {id-> idUser
//                                    transaction.paymentWalletLobby(
//                                        id,
//                                        data.userAmountLobby.toString().toDouble(),
//                                        "Bearer " + token.toString()
//                                    )
//                                        .enqueue(object : Callback<LobbyResponse> {
//                                            override fun onResponse(
//                                                call: Call<LobbyResponse>,
//                                                response: Response<LobbyResponse>
//                                            ) {
//                                                var data = response.body()
//                                                if (data != null) {
//                                                    val editor = preferencias.edit()
//                                                    val valorTotalPagar: TextView =
//                                                        view.findViewById(R.id.valor_total)
//                                                    val valorTotalRecebido: TextView =
//                                                        view.findViewById(R.id.valor_total_recebido)
//                                                    Log.e("Sucesso", "Lobby" + Gson().toJson(data))
//                                                    valorTotalPagar.text = "R$ ${data.amount}"
//                                                    valorTotalRecebido.text =
//                                                        "R$ ${data.amountTotal}"
//                                                }
//                                            }
//
//                                            override fun onFailure(
//                                                call: Call<LobbyResponse>,
//                                                t: Throwable
//                                            ) {
//                                                Log.e("Erro", "erro " + t.message)
//                                            }
//                                        })
//                                }
//                            }
//
//                        }
//
//                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
//                            Log.e("Erro", "erro " + t.message)
//                        }
//                    })
//            }
//
//        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            Sala_PagamentoFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}