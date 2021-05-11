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
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.apiConnection.Conexao
import com.apiConnection.models.response.lobby.LobbyResponse
import com.apiConnection.models.response.transaction.TransactionResponse
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
        (activity as BottomBarActivity).changeTitulo(2)
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
                            if (data.amount == data.amountTotal) {
                                val btnFinalizar: Button = view.findViewById(R.id.btn_finalizar)
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


        btnPagar.setOnClickListener {

            idUser?.toInt()?.let {
                findUserById.findUserById(it, "Bearer " + token.toString())
                    .enqueue(object : Callback<UserResponse> {
                        override fun onResponse(
                            call: Call<UserResponse>,
                            response: Response<UserResponse>
                        ) {
                            var data = response.body()

                            if (data != null) {
                                Log.e("Sucesso", "usuariossssss" + Gson().toJson(data))
                                idUser?.toInt()?.let { id ->
                                    idUser
                                    transaction.paymentWalletLobby(
                                        id,
                                        data.userAmountLobby.toString().toDouble(),
                                        "Bearer " + token.toString()
                                    )
                                        .enqueue(object : Callback<TransactionResponse> {
                                            override fun onResponse(
                                                call: Call<TransactionResponse>,
                                                response: Response<TransactionResponse>
                                            ) {
                                                var codeStatus = response.code();
                                                var message = response.message()
                                                if (codeStatus == 401) {
                                                    Toast.makeText(
                                                        context,
                                                        "Não foi possivel realizar o pagamento saldo insuficiente!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                var data = response.body()
                                                if (data != null) {
                                                    Log.e("Sucesso", "teste pagamentoooo" + Gson().toJson(data))
                                                    Toast.makeText(
                                                        context,
                                                        "Pagamento realizado com sucesso!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<TransactionResponse>,
                                                t: Throwable
                                            ) {
                                                Toast.makeText(
                                                    context,
                                                    "Não foi possivel realizar o pagamento!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                Log.e("Erro", "erro " + t.message)
                                            }
                                        })
                                }
                            }

                        }

                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            Log.e("Erro", "erro " + t.message)
                        }
                    })
            }

        }

        val btnFinalizar: Button = view.findViewById(R.id.btn_finalizar)
        btnFinalizar.setOnClickListener {
            idLobby?.toInt()?.let {
                findByLobbyUser.deleteLobby(it, "Bearer " + token.toString())
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(
                            call: Call<Void>,
                            response: Response<Void>
                        ) {
                            Toast.makeText(
                                context,
                                "Lobby finalizada com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                            val editor = preferencias.edit()
                            editor.remove("idLobby")
                            editor.apply()
                            val transaction: FragmentTransaction =
                                requireFragmentManager().beginTransaction()
                            transaction.replace(R.id.fragmentContainer, HubFragment.newInstance())
                            transaction.addToBackStack(null)
                            transaction.commit()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(
                                context,
                                "Não foi possivel finalizar a lobby verifique o valor recebido!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("Erro", "erro " + t.message)
                        }
                    })
            }
        }

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