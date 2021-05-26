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
import com.apiConnection.models.response.user.UserResponse
import kotlinx.android.synthetic.main.fragment_wallet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WalletFragment : Fragment() {
    private val findUserById = Conexao.findUserById()
    lateinit var preferencias: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as BottomBarActivity).changeTitulo(3)
        val view:View = inflater.inflate(R.layout.fragment_wallet, container, false)

        //============= recuperando dados amarzenado em cache ========================
        preferencias =
            this.activity!!.getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val idUser = preferencias.getString("idUser", null)
        val token = preferencias.getString("Auth", null)
        var nameUser = preferencias.getString("nameUser", null)
        var moneyShared = preferencias.getFloat("userAmount", 0.00F)
        // ============================================================================
        // ================== caso tenha valor e nome em cache  =======================
        var saldoContaShared: TextView = view.findViewById(R.id.valor_carteira)
        saldoContaShared.hint = "R$ ${moneyShared.toString()}"

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


        /// ===================================================


        /// ===================================================

        val btnAdicionar : Button = view.findViewById(R.id.btn_adicionar_dinheiro)
        btnAdicionar.setOnClickListener(View.OnClickListener {
            val transaction : FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.fragmentContainer, AdicionarDinheiroFragment.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        })

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