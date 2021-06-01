package com.payshare.payshareapp

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apiConnection.Conexao
import com.apiConnection.models.response.lobby.LobbyResponse
import com.apiConnection.models.response.transaction.TransactionResponse
import com.apiConnection.models.response.user.UserResponse
import com.google.gson.Gson
import com.shared.adapters.AdapterSala
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


class Sala_PagamentoFragment : Fragment() {

    private val findByLobbyUser = Conexao.findByLobbyUser()
    private val conn = Conexao
    private val findUserById = Conexao.findUserById()
    private val transaction = Conexao.createTransactionWallet()
    lateinit var preferencias: SharedPreferences
    lateinit var rvDinamic: RecyclerView
    lateinit var salaAdapter: AdapterSala
    lateinit var idUser: String
    lateinit var token: String
    lateinit var idLobby : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as BottomBarActivity).changeTitulo(2)
        val view: View = inflater.inflate(R.layout.fragment_sala_pagamento, container, false)
        //============= recuperando dados amarzenado em cache ========================
        preferencias =
            this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        idUser = preferencias.getString("idUser", null)!!
        token = preferencias.getString("Auth", null)!!
        var nameUser = preferencias.getString("nameUser", null)
        var moneyShared = preferencias.getFloat("userAmount", 0.00F)
        idLobby = preferencias.getString("idLobby", null)!!

        //============= recuperando recycleView ========================
        rvDinamic = view.findViewById(R.id.rv_extrato)
        var layout = LinearLayoutManager(this.context)
        layout.orientation = RecyclerView.VERTICAL
        rvDinamic.layoutManager = layout
        listParticipantes()
        salaAdapter = AdapterSala(this)
        salaAdapter.notifyDataSetChanged()
        //========================================================



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
                        val btnFinalizar: Button = view.findViewById(R.id.btn_finalizar)
                        var data = response.body()
                        if (data != null) {
                            val editor = preferencias.edit()
                            val valorTotalPagar: TextView = view.findViewById(R.id.valor_total)
                            val valorTotalRecebido: TextView =
                                view.findViewById(R.id.valor_total_recebido)
                            Log.e("Sucesso", "Lobby" + Gson().toJson(data))
                            val dec = DecimalFormat("#,###.00")
                            valorTotalPagar.text = "R$ ${dec.format(data.amount)}"
                            valorTotalRecebido.text = if (data.amountTotal == 0.00) "R$ 0.00" else  "R$ ${dec.format(data.amountTotal)}"
                            if(data.amountTotal >= 0) {
                                if(data.amountTotal != data.amount) {
                                    conn.findUserById().findUserById(idUser.toInt(), "Bearer $token")
                                        .enqueue(object : Callback<UserResponse>{
                                            override fun onResponse(
                                                call: Call<UserResponse>,
                                                response: Response<UserResponse>
                                            ) {
                                                var userData = response.body()

                                                if(userData != null) {
                                                    Log.println(Log.INFO, "sda", userData.toString())

                                                    if(userData.userAmountLobby ==  0.0) {
                                                        var id: Int = resources.getIdentifier("bg_button_clear_red", "drawable", activity?.packageName)
                                                        btnFinalizar.isClickable = false
                                                        btnFinalizar.background = resources.getDrawable(id)
                                                        btnFinalizar.text = "Aguardando outros Pagamentos"
                                                        btnFinalizar.setTextColor(
                                                            Color.parseColor("#f0f0f0"))
                                                        btnFinalizar.visibility = View.VISIBLE
                                                        btnPagar.visibility = View.GONE
                                                    }
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<UserResponse>,
                                                t: Throwable
                                            ) {
                                                Log.e("Erro", "erro " + t.message)
                                            }

                                        })
                                } else {
                                    Log.println(Log.INFO, "sd222a", data.toString())
                                    var id: Int = resources.getIdentifier("bg_button_normal", "drawable",  activity?.packageName)
                                    btnFinalizar.isClickable = true
                                    btnFinalizar.background = resources.getDrawable(id)
                                    btnFinalizar.text = "Finalizar Lobby"
                                    btnFinalizar.setTextColor(Color.parseColor("#f0f0f0"))
                                    btnPagar.visibility = View.GONE
                                    btnFinalizar.visibility = View.VISIBLE
                                }
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
//                                Log.e("Sucesso", "usuariossssss" + Gson().toJson(data))
//                                idUser?.toInt()?.let { id ->
//                                    idUser
//                                    transaction.paymentWalletLobby(
//                                        id,
//                                        data.userAmountLobby.toString().toDouble(),
//                                        "Bearer " + token.toString()
//                                    )
//                                        .enqueue(object : Callback<TransactionResponse> {
//                                            override fun onResponse(
//                                                call: Call<TransactionResponse>,
//                                                response: Response<TransactionResponse>
//                                            ) {
//                                                var codeStatus = response.code();
//                                                var message = response.message()
//                                                if (codeStatus == 401) {
//                                                    Toast.makeText(
//                                                        context,
//                                                        "Não foi possivel realizar o pagamento saldo insuficiente!",
//                                                        Toast.LENGTH_SHORT
//                                                    ).show()
//                                                }
//                                                var data = response.body()
//                                                if (data != null) {
//                                                    Log.e("Sucesso", "teste pagamentoooo" + Gson().toJson(data))
//                                                    Toast.makeText(
//                                                        context,
//                                                        "Pagamento realizado com sucesso!",
//                                                        Toast.LENGTH_SHORT
//                                                    ).show()
//                                                }
//                                            }
//
//                                            override fun onFailure(
//                                                call: Call<TransactionResponse>,
//                                                t: Throwable
//                                            ) {
//                                                Toast.makeText(
//                                                    context,
//                                                    "Não foi possivel realizar o pagamento!",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
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

        val btnPrePay: Button = view.findViewById(R.id.btn_pagar_lobby)
        btnPrePay.setOnClickListener{

            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer, EscolherFormaPagamentoFragment.newInstance())
            transaction.addToBackStack("Sala_PagamentoFragment")
            transaction.commit()
        }

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
//                                Log.e("Sucesso", "usuariossssss" + Gson().toJson(data))
//                                idUser?.toInt()?.let { id ->
//                                    idUser
//                                    transaction.createTransactionLobbyMercadoPago(
//                                        id,
//                                        data.userAmountLobby.toString().toDouble(),
//                                        "Bearer " + token.toString()
//                                    )
//                                        .enqueue(object : Callback<TransactionWalletResponse> {
//                                            override fun onResponse(
//                                                call: Call<TransactionWalletResponse>,
//                                                body: Response<TransactionWalletResponse>
//                                            ) {
//                                                var codeStatus = body.code();
//                                                var message = body.message()
//                                                if (codeStatus == 401) {
//                                                    Toast.makeText(
//                                                        context,
//                                                        "Não foi possivel realizar!",
//                                                        Toast.LENGTH_SHORT
//                                                    ).show()
//                                                }
//
//                                                var data = body.body()
//                                                if (data != null) {
//                                                    val editor = preferencias.edit()
//                                                    Log.e("Sucesso", "Initpoint" + Gson().toJson(data))
//                                                    editor.putString("initPoint", data.body.initPoint)
//                                                    editor.apply()
//                                                    Log.e("Sucesso", "Transaction lobby " + Gson().toJson(data))
//                                                    val intent =
//                                                        Intent(context, MercadoPagoCheckout::class.java)
//                                                    startActivity(intent)
//                                                }
//                                            }
//
//                                            override fun onFailure(
//                                                call: Call<TransactionWalletResponse>,
//                                                t: Throwable
//                                            ) {
//                                                Toast.makeText(
//                                                    context,
//                                                    "Não foi possivel realizar o pagamento!",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
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

    fun listParticipantes(){
        findByLobbyUser.findLobbyById(this.idLobby.toInt(),"Bearer $token").enqueue(object :
            Callback<LobbyResponse> {
            override fun onResponse(call: Call<LobbyResponse>, response: Response<LobbyResponse>) {
                var data = response.body()
                if (data != null) {
                    salaAdapter.addTransaction(data.userPfList)
                }
                rvDinamic.adapter = salaAdapter
            }

            override fun onFailure(call: Call<LobbyResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            Sala_PagamentoFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}