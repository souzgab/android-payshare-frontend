package com.payshare.payshareapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.apiConnection.Conexao
import com.apiConnection.models.response.user.UserResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_hub.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HubFragment : Fragment() {

    private val findUserById = Conexao.findUserById()
    lateinit var preferencias: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_hub, container, false)

        //============= recuperando dados amarzenado em cache ========================
        preferencias =
            this.activity!!.getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val idUser = preferencias.getString("idUser", null)
        val token = preferencias.getString("Auth", null)
        var nameUser = preferencias.getString("nameUser", null)
        var moneyShared = preferencias.getFloat("userAmount", 0.00F)
        // ============================================================================
        // ================== caso tenha valor e nome em cache  =======================
        var saldoContaShared: TextView = view.findViewById(R.id.txt_valor_saldo)
        saldoContaShared.hint = "R$ ${moneyShared.toString()}"

        var nomeUser: TextView = view.findViewById(R.id.name_user)
        nomeUser.text = nameUser
        // ============================================================================

        //========= Busca informações do usuario ======================================
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
                            var saldoConta: TextView = view.findViewById(R.id.txt_valor_saldo)
                            var nomeUser: TextView = view.findViewById(R.id.name_user)
                            editor.putFloat("userAmount", data.userAmount.toFloat())
                            editor.apply()
                            nomeUser.text = nameUser
                            saldoConta.hint = "R$ ${data.userAmount}"
                            Log.e("Sucesso", "Usuario" + Gson().toJson(data))
                        }

                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        Log.e("Erro", "erro " + t.message)
                    }
                })
        }
        //================================ fim da consulta =======================================

        /// ESTA PARTE FAZ UM BUSCA NO BANCO PARA REVELAR O DINHEIRO
        var saldoConta: TextView = view.findViewById(R.id.txt_valor_saldo)
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
                                        view.findViewById(R.id.txt_valor_saldo)
                                    var nomeUser: TextView = view.findViewById(R.id.name_user)
                                    editor.putFloat("userAmount", data.userAmount.toFloat())
                                    editor.apply()
                                    nomeUser.text = nameUser
                                    saldoConta.hint = "R$ ${data.userAmount}"
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

        val btnProfile : ImageView = view.findViewById(R.id.img_user_hub)
        btnProfile.setOnClickListener(View.OnClickListener {
            val transaction : FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.fragmentContainer, PerfilFragment.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        })




        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HubFragment().apply {
                arguments = Bundle().apply {}
            }
    }

}