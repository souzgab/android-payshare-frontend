package com.payshare.payshareapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentTransaction
import com.apiConnection.Conexao
import com.apiConnection.dataClassAdapter.payments.PaymentFindCard
import com.apiConnection.models.response.payment.PaymentFindCardResponse
import com.apiConnection.models.response.user.UserResponse
import kotlinx.android.synthetic.main.fragment_wallet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


class WalletFragment : Fragment() {
    private val findUserById = Conexao.findUserById()
    lateinit var preferencias: SharedPreferences
    private val conn = Conexao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as BottomBarActivity).changeTitulo(3)
        val view: View = inflater.inflate(R.layout.fragment_wallet, container, false)
        preferencias = this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        //Setando cartão Padrão

        Log.println(Log.INFO, "sd", preferencias.all.toString())

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
                    val editor = preferencias.edit()
                    editor.putString("cardNumber", data.cardNumber)
                    editor.putString("cardName", data.cardName)
                    editor.putString("cardNumberFourStart", data.cardNumberFourStart)
                    editor.putString("cardNumberFourEnd", data.cardNumberFourEnd)
                    editor.putString("expiryDate", data.expiryDate)
                    editor.putString("type", data.type)
                    editor.apply()

                    view.findViewById<TextView>(R.id.txt_cardNameWallet).text = cardName
                    view.findViewById<TextView>(R.id.cardNumberWallet).text = "$cardNumberFourStart ******** $cardNumberFourEnd"
                    view.findViewById<TextView>(R.id.tipo_cartaoWallet).text = type
                }

            }

            override fun onFailure(call: Call<PaymentFindCardResponse>, t: Throwable) {
                Log.e("Erro", "erro " + t.message)
            }
        })

        // == >

        //============= recuperando dados amarzenado em cache ========================
        preferencias =
            this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val idUser = preferencias.getString("idUser", null)
        val token = preferencias.getString("Auth", null)
        var nameUser = preferencias.getString("nameUser", null)
        var moneyShared = preferencias.getFloat("userAmount", 0.00F)
        // ============================================================================
        // ================== caso tenha valor e nome em cache  =======================
        var saldoContaShared: TextView = view.findViewById(R.id.valor_carteira)
        val dec = DecimalFormat("#,###.00")
        saldoContaShared.hint = if (moneyShared.toString() == "0.00") "R$ 0.00" else  "R$ ${dec.format(moneyShared)}"

        // ============================================================================
        /// ESTA PARTE FAZ UM BUSCA NO BANCO PARA REVELAR O DINHEIRO
        var saldoConta: TextView = view.findViewById(R.id.valor_carteira)
        var btnOculta: ImageView = view.findViewById(R.id.img_visualizar_saldo)
        var btnEstado: Boolean = true

        btnOculta.setOnClickListener(View.OnClickListener {
            if (btnEstado) {
                saldoConta.hint = "******"
                btnOculta.setImageResource(R.drawable.btn_ocultar_money)
                btnEstado = false
            } else {
                idUser?.toInt()?.let {
                    findUserById.findUserById(it, "Bearer " + token.toString())
                        .enqueue(object : Callback<UserResponse> {
                            override fun onResponse(
                                call: Call<UserResponse>,
                                response: Response<UserResponse>
                            ) {
                                var data = response.body()

                                if (data != null) {
                                    val editor = preferencias.edit()
                                    var saldoConta: TextView =
                                        view.findViewById(R.id.valor_carteira)
                                    editor.putFloat("userAmount", data.userAmount.toFloat())
                                    editor.apply()
                                    val dec = DecimalFormat("#,###.00")
                                    saldoConta.hint = if (data.userAmount == 0.00) "R$ 0.00" else  "R$ ${dec.format(data.userAmount)}"
                                    btnOculta.setImageResource(R.drawable.btn_show_money)
                                    btnEstado = true
                                }

                            }

                            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                                Log.e("Erro", "erro " + t.message)
                            }
                        })
                }
            }
        })


        /// ===================================================

        if (cardNumberFourStart != null) {
            when {
                cardNumberFourStart.startsWith("4") -> {
                    view.findViewById<ImageView>(R.id.img_cartaoWallet).setImageResource(R.drawable.visa_card)
                }
                cardNumberFourStart.startsWith("5") -> {
                    view.findViewById<ImageView>(R.id.img_cartaoWallet).setImageResource(R.drawable.master_card)
                }
            }
        }

        /// ===================================================

        val btnAdicionar : Button = view.findViewById(R.id.btn_adicionar_dinheiro)
        btnAdicionar.setOnClickListener(View.OnClickListener {
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer, AdicionarDinheiroFragment.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        })

        val btnAdicionarFormaDePagamento: Button = view.findViewById(R.id.btn_adicionar_pagamento_forma)
        btnAdicionarFormaDePagamento.setOnClickListener{
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer, AdicionarFormaPagamentoFragment.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view

    }



    companion object {
      @JvmStatic
      fun newInstance() =
            WalletFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}