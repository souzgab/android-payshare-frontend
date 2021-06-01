package com.payshare.payshareapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import com.apiConnection.Conexao
import com.apiConnection.dataClassAdapter.payments.PaymentFindCard
import com.apiConnection.dataClassAdapter.payments.PaymentPostCard
import com.apiConnection.models.response.lobby.LobbyResponse
import com.apiConnection.models.response.payment.PaymentFindCardResponse
import com.apiConnection.models.response.transaction.TransactionResponse
import com.apiConnection.models.response.transaction.TransactionWalletResponse
import com.apiConnection.models.response.user.UserResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


class EscolherFormaPagamentoFragment : Fragment() {

    private val conn = Conexao
    private val findByLobbyUser = Conexao.findByLobbyUser()
    private val transaction = Conexao.createTransactionWallet()
    private val findUserById = Conexao.findUserById()
    lateinit var preferencias: SharedPreferences
    lateinit var idUser: String
    lateinit var token: String
    lateinit var radio: RadioButton

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(com.payshare.payshareapp.R.layout.fragment_pre_payment, container, false)
            preferencias = this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        idUser = preferencias.getString("idUser", null)!!
        token = preferencias.getString("Auth", null)!!
        var moneyShared = preferencias.getFloat("userAmount", 0.00F)
        val dec = DecimalFormat("#,###.00")
        val editor = preferencias.edit()

        var id: Int = resources.getIdentifier("bg_button_clear_red", "drawable",
            activity?.packageName
        )

        view.findViewById<Button>(R.id.btn_pagarPre).isClickable = false
        view.findViewById<Button>(R.id.btn_pagarPre).background = resources.getDrawable(id)
        view.findViewById<Button>(R.id.btn_pagarPre).text = "Selecione um Tipo"
        view.findViewById<Button>(R.id.btn_pagarPre).setTextColor(Color.parseColor("#f0f0f0"))

        view.findViewById<CardView>(R.id.box_pagamentoPre).visibility = View.GONE
        view.findViewById<TextView>(R.id.et_nomeCartaoPre).hint =
            if (moneyShared.toString() == "0.00") "Saldo: R$ 0.00"
            else "Saldo: R$ ${dec.format(moneyShared)}"

        val cardNumber = preferencias.getString("cardNumber", null)
        val cvv = preferencias.getString("cvv", null)
        val cardName = preferencias.getString("cardName", null)
        val cardNumberFourStart = preferencias.getString("cardNumberFourStart", null)
        val cardNumberFourEnd = preferencias.getString("cardNumberFourEnd", null)
        val expiryDate = preferencias.getString("expiryDate", null)
        val type = preferencias.getString("type", null)

        var card = PaymentFindCard(cardNumber!!,cvv!!)
        var dateInit = ""
        var dateEnd = ""

        conn.wallet().findCard(card).enqueue(object : Callback<PaymentFindCardResponse>{
            override fun onResponse(
                call: Call<PaymentFindCardResponse>,
                response: Response<PaymentFindCardResponse>
            ) {
                val data = response.body()
                if(data != null) {
                    dateInit = data.expiryDate.substring(0, 2)
                    dateEnd =  data.expiryDate.substring(2, 4)

                    editor.putString("cardNumber", data.cardNumber)
                    editor.putString("cardName", data.cardName)
                    editor.putString("cardNumberFourStart", data.cardNumberFourStart)
                    editor.putString("cardNumberFourEnd", data.cardNumberFourEnd)
                    editor.putString("expiryDate", data.expiryDate)
                    editor.putString("type", data.type)
                    editor.apply()
                }

            }

            override fun onFailure(call: Call<PaymentFindCardResponse>, t: Throwable) {
                Log.e("Erro", "erro " + t.message)
            }
        })

        view.findViewById<Button>(R.id.btn_escolherCartao).setOnClickListener {
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer, EncontrarCartaoFragment.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        view.findViewById<RadioButton>(R.id.wallet).setOnClickListener {
            radio = view.findViewById(R.id.wallet)
            view.findViewById<RadioButton>(R.id.cartao).isChecked = false
            view.findViewById<RadioButton>(R.id.mercadoPago).isChecked = false

            id = resources.getIdentifier("bg_button_normal", "drawable",
            activity?.packageName
        )
            view.findViewById<Button>(R.id.btn_pagarPre).isClickable = true
            view.findViewById<Button>(R.id.btn_pagarPre).background = resources.getDrawable(id)
            view.findViewById<Button>(R.id.btn_pagarPre).text = "Pagar"
            view.findViewById<Button>(R.id.btn_pagarPre).setTextColor(Color.parseColor("#f0f0f0"))
        }

        view.findViewById<RadioButton>(R.id.cartao).setOnClickListener {
            radio = view.findViewById(R.id.cartao)
            view.findViewById<RadioButton>(R.id.wallet).isChecked = false
            view.findViewById<RadioButton>(R.id.mercadoPago).isChecked = false

            id = resources.getIdentifier("bg_button_normal", "drawable",
                activity?.packageName)
            view.findViewById<Button>(R.id.btn_pagarPre).isClickable = true
            view.findViewById<Button>(R.id.btn_pagarPre).background = resources.getDrawable(id)
            view.findViewById<Button>(R.id.btn_pagarPre).text = "Pagar"
            view.findViewById<Button>(R.id.btn_pagarPre).setTextColor(Color.parseColor("#f0f0f0"))
        }

        view.findViewById<RadioButton>(R.id.mercadoPago).setOnClickListener {
            radio = view.findViewById(R.id.mercadoPago)
            view.findViewById<RadioButton>(R.id.cartao).isChecked = false
            view.findViewById<RadioButton>(R.id.wallet).isChecked = false

            id = resources.getIdentifier("bg_button_normal", "drawable",
                activity?.packageName)
            view.findViewById<Button>(R.id.btn_pagarPre).isClickable = true
            view.findViewById<Button>(R.id.btn_pagarPre).background = resources.getDrawable(id)
            view.findViewById<Button>(R.id.btn_pagarPre).text = "Pagar"
            view.findViewById<Button>(R.id.btn_pagarPre).setTextColor(Color.parseColor("#f0f0f0"))
        }

        view.findViewById<RadioGroup>(R.id.rd_typePagamentoPre).setOnCheckedChangeListener { radioGroup, i ->

            // Pagamentos Setados de Acordo com o Button
            when(radioGroup.checkedRadioButtonId) {
               R.id.cartao -> {
                   view.findViewById<TextView>(R.id.et_nomeCartaoPre).visibility = View.INVISIBLE
                   view.findViewById<CardView>(R.id.box_pagamentoPre).visibility = View.VISIBLE
                   view.findViewById<TextView>(R.id.txt_pagamento).text = cardName
                   view.findViewById<TextView>(R.id.numero_cartao).text = cardNumber
                   view.findViewById<TextView>(R.id.tv_expire).text = "Validade: $dateInit/$dateEnd"
                   view.findViewById<TextView>(R.id.tipo_cartao).text = type

                   view.findViewById<Button>(R.id.btn_pagarPre).setOnClickListener {
                       idUser?.toInt()?.let {
                           findUserById.findUserById(it, "Bearer $token")
                               .enqueue(object : Callback<UserResponse> {
                                   override fun onResponse(
                                       call: Call<UserResponse>,
                                       response: Response<UserResponse>
                                   ) {
                                       var data = response.body()
                                       if (data != null) {
                                           idUser?.toInt()?.let { id ->
                                               transaction.paymentCard(
                                                   id,
                                                   data.userAmountLobby.toString().toDouble(),
                                                   "Bearer $token"
                                               ).enqueue(object : Callback<TransactionResponse> {
                                                   override fun onResponse(
                                                       call: Call<TransactionResponse>,
                                                       response: Response<TransactionResponse>
                                                   ) {
                                                       Log.println(Log.INFO, "rs", response.body().toString())
                                                       var codeStatus = response.code();
                                                       if (codeStatus == 401) {
                                                           Toast.makeText(
                                                               context,
                                                               "Não foi possivel realizar o pagamento saldo insuficiente!",
                                                               Toast.LENGTH_SHORT
                                                           ).show()
                                                       }
                                                       var data = response.body()
                                                       if (data != null && response.isSuccessful) {
                                                           Toast.makeText(
                                                               context,
                                                               "Pagamento realizado com sucesso!",
                                                               Toast.LENGTH_SHORT
                                                           ).show()

                                                           val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
                                                           transaction.replace(R.id.fragmentContainer, Sala_PagamentoFragment.newInstance())
                                                           transaction.addToBackStack(null)
                                                           transaction.commit()
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
               }

               R.id.wallet -> {
                   view.findViewById<TextView>(R.id.et_nomeCartaoPre).visibility = View.VISIBLE
                   view.findViewById<CardView>(R.id.box_pagamentoPre).visibility = View.INVISIBLE

                   // Pagamento Setado para Wallet
                   view.findViewById<Button>(R.id.btn_pagarPre).setOnClickListener {
                       idUser?.toInt()?.let {
                           findUserById.findUserById(it, "Bearer $token")
                               .enqueue(object : Callback<UserResponse> {
                                   override fun onResponse(
                                       call: Call<UserResponse>,
                                       response: Response<UserResponse>
                                   ) {
                                       var data = response.body()
                                       if (data != null) {
                                           idUser?.toInt()?.let { id ->
                                               idUser
                                               transaction.paymentWalletLobby(
                                                   id,
                                                   data.userAmountLobby.toString().toDouble(),
                                                   "Bearer $token"
                                               ).enqueue(object : Callback<TransactionResponse> {
                                                       override fun onResponse(
                                                           call: Call<TransactionResponse>,
                                                           response: Response<TransactionResponse>
                                                       ) {
                                                           var codeStatus = response.code();
                                                           if (codeStatus == 401) {
                                                               Toast.makeText(
                                                                   context,
                                                                   "Não foi possivel realizar o pagamento saldo insuficiente!",
                                                                   Toast.LENGTH_SHORT
                                                               ).show()
                                                           }
                                                           var data = response.body()
                                                           if (data != null && response.isSuccessful) {
                                                               Toast.makeText(
                                                                   context,
                                                                   "Pagamento realizado com sucesso!",
                                                                   Toast.LENGTH_SHORT
                                                               ).show()

                                                               val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
                                                               transaction.replace(R.id.fragmentContainer, Sala_PagamentoFragment.newInstance())
                                                               transaction.addToBackStack(null)
                                                               transaction.commit()
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
               }

               R.id.mercadoPago -> {
                   view.findViewById<TextView>(R.id.et_nomeCartaoPre).visibility = View.INVISIBLE
                   view.findViewById<CardView>(R.id.box_pagamentoPre).visibility = View.GONE

                   view.findViewById<Button>(R.id.btn_pagarPre).setOnClickListener {
                   view.findViewById<ProgressBar>(R.id.pgPre).visibility = View.VISIBLE
                    idUser?.toInt()?.let {
                    findUserById.findUserById(it, "Bearer $token").enqueue(object : Callback<UserResponse> {
                        override fun onResponse(
                            call: Call<UserResponse>,
                            response: Response<UserResponse>
                        ) {
                            var data = response.body()
                            if (data != null) {
                                idUser?.toInt()?.let { id ->
                                    idUser
                                    transaction.createTransactionLobbyMercadoPago(
                                        id,
                                        data.userAmountLobby.toString().toDouble(),
                                        "Bearer $token"
                                    )
                                    .enqueue(object : Callback<TransactionWalletResponse> {
                                            override fun onResponse(
                                                call: Call<TransactionWalletResponse>,
                                                body: Response<TransactionWalletResponse>
                                            ) {
                                                var codeStatus = body.code();
                                                var message = body.message()
                                                if (codeStatus == 401) {
                                                    view.findViewById<ProgressBar>(R.id.pgPre).visibility = View.GONE
                                                    Toast.makeText(
                                                        context,
                                                        "Não foi possivel realizar!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                                var data = body.body()
                                                if (data != null) {
                                                    val editor = preferencias.edit()
                                                    view.findViewById<ProgressBar>(R.id.pgPre).visibility = View.GONE
                                                    editor.putString("initPoint", data.body.initPoint)
                                                    editor.apply()
                                                    startActivity(Intent(context, MercadoPagoCheckout::class.java))
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<TransactionWalletResponse>,
                                                t: Throwable
                                            ) {
                                                view.findViewById<ProgressBar>(R.id.pgPre).visibility = View.GONE
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
               }
            }
        }

        // Carregar informações do usuário
        idUser?.toInt()?.let {
            findByLobbyUser.findLobbyUser(it, "Bearer " + token.toString())
                .enqueue(object : Callback<LobbyResponse> {
                    override fun onResponse(
                        call: Call<LobbyResponse>,
                        response: Response<LobbyResponse>
                    ) {
                        var data = response.body()
                        if (data != null) {
                            Log.println(Log.INFO, "", response.body().toString())
                            if (data.amount === data.amountTotal) {
                                val btnFinalizar: Button = view.findViewById(R.id.btn_finalizar)
                                btnFinalizar.visibility = View.VISIBLE
                                view.findViewById<Button>(R.id.btn_pagarPre).visibility = View.INVISIBLE
                            }
                        }
                    }

                    override fun onFailure(call: Call<LobbyResponse>, t: Throwable) {
                        Log.e("Erro", "erro " + t.message)
                    }
                })
        }

        return view
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            EscolherFormaPagamentoFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
