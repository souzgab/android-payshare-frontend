package com.payshare.payshareapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.apiConnection.Conexao
import com.apiConnection.dataClassAdapter.payments.PaymentPostCard
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdicionarFormaPagamentoFragment : Fragment() {

    private val conn = Conexao
    lateinit var preferencias: SharedPreferences
    lateinit var idUser: String
    lateinit var token: String
    lateinit var radio: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(com.payshare.payshareapp.R.layout.fragment_adicionar_forma_pagamento, container, false)
            preferencias = this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        radio = view.findViewById(R.id.credito)
        idUser = preferencias.getString("idUser", null)!!
        token = preferencias.getString("Auth", null)!!

        view.findViewById<RadioButton>(R.id.credito).setOnClickListener {
            radio = view.findViewById(R.id.credito)
            view.findViewById<RadioButton>(R.id.debito).isChecked = false
        }

        view.findViewById<RadioButton>(R.id.debito).setOnClickListener {
            radio = view.findViewById(R.id.debito)
            view.findViewById<RadioButton>(R.id.credito).isChecked = false
        }

        view.findViewById<Button>(R.id.btn_inserir).setOnClickListener {
            val card = getCardOnView(view)
            if( card != null) {
                val builder = AlertDialog.Builder(view.context)

                // set title
                builder.setTitle("Adição de valor na carteira")

                //set content area
                builder.setMessage("Tem certeza que deseja adicionar R$ ${card.cardNumber} em sua carteira ?")

                builder.setPositiveButton(
                    "Confirmar"
                ) { dialog, id ->

                    Log.println(Log.INFO, "text", card.toString())
                    conn.wallet().postCard(card).enqueue(object :
                        Callback<Any> {
                        override fun onResponse(call: Call<Any>, response: Response<Any>) {
                            if (response.isSuccessful) {

                                fragmentManager!!.beginTransaction().replace(
                                    R.id.fragmentContainer,
                                    WalletFragment.newInstance()
                                ).commit()
                                Toast.makeText(context, "Sucesso!", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Any>, t: Throwable) {
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

        return view
    }

    private fun getCardOnView(view: View): PaymentPostCard? {
        if (idUser != null) {
            return PaymentPostCard(
                idUser.toInt(),
                view.findViewById<EditText>(R.id.et_cvv).text.toString(),
                view.findViewById<EditText>(R.id.et_NmrCartao).text.toString(),
                view.findViewById<EditText>(R.id.etdataValidade).text.toString(),
                view.findViewById<EditText>(R.id.et_nomeCard).text.toString(),
                radio.text.toString()
            )
        } else {
            return null
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AdicionarFormaPagamentoFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
