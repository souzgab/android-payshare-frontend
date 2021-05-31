package com.payshare.payshareapp

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.apiConnection.Conexao
import com.apiConnection.dataClassAdapter.payments.PaymentFindCard
import com.apiConnection.models.response.payment.PaymentFindCardResponse
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EncontrarCartaoFragment: Fragment() {

    private val conn = Conexao
    lateinit var preferencias: SharedPreferences
    lateinit var idUser: String
    lateinit var token: String
    lateinit var radio: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater.inflate(com.payshare.payshareapp.R.layout.fragment_encontrar_cartao, container, false)
        preferencias = this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        idUser = preferencias.getString("idUser", null)!!
        token = preferencias.getString("Auth", null)!!
        val editor = preferencias.edit()
        var card = PaymentFindCard("", "")
        view.findViewById<CardView>(R.id.box_pagamentoE).visibility = View.GONE

        view.findViewById<Button>(R.id.btn_escolherCartao).setOnClickListener {
            requireFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer,
                AdicionarFormaPagamentoFragment.newInstance()
            ).commit()
        }

        view.findViewById<Button>(R.id.btn_pesquisarE).setOnClickListener {
            if (validCard(view)) {
                card.cardNumber = view.findViewById<EditText>(R.id.et_NmrCartaoE).text.toString()
                card.cvv = view.findViewById<EditText>(R.id.et_cvvE).text.toString()
                if( card != null) {
                    val builder = AlertDialog.Builder(view.context)

                    // set title
                    builder.setTitle("Encontrar Cartão")

                    //set content area
                    builder.setMessage("O Cartão:${card.cardNumber} está correto?")

                    builder.setPositiveButton(
                        "Confirmar"
                    ) { dialog, id ->

                        Log.println(Log.INFO, "text", card.toString())
                        conn.wallet().findCard(card).enqueue(object :
                            Callback<PaymentFindCardResponse> {
                            override fun onResponse(call: Call<PaymentFindCardResponse>, response: Response<PaymentFindCardResponse>) {
                                if (response.isSuccessful) {

                                    var data = response.body()

                                    if (data != null) {
                                        var dateInit = data.expiryDate.substring(0, 2)
                                        var dateEnd = data.expiryDate.substring(2, 4)

                                        view.findViewById<CardView>(R.id.box_pagamentoE).visibility = View.VISIBLE
                                        view.findViewById<TextView>(R.id.numeroCartaoE).text = data.cardNumber
                                        view.findViewById<TextView>(R.id.txt_pagamento).text = data.cardName
                                        view.findViewById<TextView>(R.id.tv_expire).text = "Validade: $dateInit/$dateEnd"
                                        view.findViewById<TextView>(R.id.tipo_cartao).text = data.type
                                        view.findViewById<Button>(R.id.btn_pesquisarE).visibility = View.GONE
                                        view.findViewById<Button>(R.id.btn_confirmar).visibility = View.VISIBLE
                                        view.findViewById<Button>(R.id.limparPesquisa).visibility = View.VISIBLE

                                        editor.putString("cardNumber", data.cardNumber)
                                        editor.putString("cardName", data.cardName)
                                        editor.putString("cardNumberFourStart", data.cardNumberFourStart)
                                        editor.putString("cardNumberFourEnd", data.cardNumberFourEnd)
                                        editor.putString("expiryDate", data.expiryDate)
                                        editor.putString("type", data.type)
                                        editor.apply()
                                    }
                                }
                            }

                            override fun onFailure(call: Call<PaymentFindCardResponse>, t: Throwable) {
                                Toast.makeText(
                                    context,
                                    "Valor não pode estar em branco",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                    }

                    builder.setNegativeButton(
                        "Cancelar"
                    ) { dialog, id ->
                        // User cancelled the dialog
                    }

                    builder.show()
                } else {
                    Toast.makeText(context, "Cartão é inválido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.findViewById<Button>(R.id.btn_confirmar).setOnClickListener {
            requireFragmentManager().beginTransaction().replace(
                R.id.fragmentContainer,
                Sala_PagamentoFragment.newInstance()
            ).commit()
            Toast.makeText(context, "Sucesso!", Toast.LENGTH_SHORT).show()
        }


        view.findViewById<Button>(R.id.limparPesquisa).setOnClickListener {
            view.findViewById<CardView>(R.id.box_pagamentoE).visibility = View.GONE
            view.findViewById<TextView>(R.id.numeroCartaoE).text = ""
            view.findViewById<TextView>(R.id.txt_pagamento).text = ""
            view.findViewById<TextView>(R.id.tv_expire).text = ""
            view.findViewById<TextView>(R.id.tipo_cartao).text = ""
            view.findViewById<Button>(R.id.btn_pesquisarE).visibility = View.VISIBLE
            view.findViewById<Button>(R.id.btn_confirmar).visibility = View.GONE
            view.findViewById<Button>(R.id.limparPesquisa).visibility = View.INVISIBLE

            editor.putString("cardNumber", "")
            editor.putString("cardNumberFourStart", "")
            editor.putString("cardNumberFourEnd", "")
            editor.putString("expiryDate", "")
            editor.putString("type", "")
            editor.apply()
        }


        return view
    }

    private fun validCard(view: View): Boolean {
        when {
            view.findViewById<EditText>(R.id.et_NmrCartaoE).text.isEmpty() -> {
                view.findViewById<EditText>(R.id.et_NmrCartaoE).error = "Não pode estar vazio"
                return false
            }
            view.findViewById<EditText>(R.id.et_cvvE).text.isEmpty() -> {
                view.findViewById<EditText>(R.id.et_cvvE).error = "Não pode estar vazio"
                return false
            }
        }
        return true
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EncontrarCartaoFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}