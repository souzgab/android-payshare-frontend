package com.payshare.payshareapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.apiConnection.Conexao
import com.apiConnection.models.response.lobby.LobbyResponse
import com.apiConnection.models.response.user.UserResponse
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bottom_bar.*
import kotlinx.android.synthetic.main.fragment_hub.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HubFragment : Fragment() {

    private val findUserById = Conexao.findUserById()
    private val findByLobbyUser = Conexao.findByLobbyUser()
    lateinit var preferencias: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_hub, container, false)
        val bottom : View  = inflater.inflate(R.layout.activity_bottom_bar, container, false)

        //============= recuperando dados amarzenado em cache ========================
        preferencias =
            this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val idUser = preferencias.getString("idUser", null)
        val token = preferencias.getString("Auth", null)
        var nameUser = preferencias.getString("nameUser", null)
        var moneyShared = preferencias.getFloat("userAmount", 0.00F)
        var idLobby = preferencias.getString("idLobby", null)
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
                            val boxAtivaLobby : ScrollView = view.findViewById(R.id.box_lobby_ativa)
                            val lobbyName : TextView = view.findViewById(R.id.txt_nome_ativo_lobby)
                            boxAtivaLobby.visibility = View.VISIBLE
                            editor.putString("idLobby", data.id.toString())
                            editor.apply()
                            lobbyName.text = "${data.lobbyDescription}"
                            Log.e("Sucesso", "idLobbyyyyyy" + idLobby.toString())
                            Log.e("Sucesso", "lobbyUser" + Gson().toJson(data))
                        } else {
                            val editor = preferencias.edit()
                            editor.remove("idLobby")
                            editor.apply()
                            val boxAtivaLobby : ScrollView = view.findViewById(R.id.box_lobby_ativa)
                            val boxLobbyOff : ScrollView = view.findViewById(R.id.box_criar_lobby)
                            boxAtivaLobby.visibility = View.INVISIBLE
                            boxLobbyOff.visibility = View.VISIBLE
                        }

                    }

                    override fun onFailure(call: Call<LobbyResponse>, t: Throwable) {
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
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer, PerfilFragment.newInstance())
            transaction.addToBackStack(PerfilFragment::class.java.simpleName)
            transaction.commit()
        })
        val btnLobby : Button = view.findViewById(R.id.btn_criar_lobby)
        btnLobby.setOnClickListener(View.OnClickListener {
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer, CriarLobbyFragment.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        })

        val btnAdicionaDinheiro : ImageView = view.findViewById(R.id.img_add_dinheiro)
        btnAdicionaDinheiro.setOnClickListener {
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer, AdicionarDinheiroFragment.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val btnTransferir : ImageView = view.findViewById(R.id.img_transferir)
        btnTransferir.setOnClickListener {
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer, TransferenciaEntreContas.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val btnEntrarLobby : AppCompatButton = view.findViewById(R.id.btn_entrar_lobby)
        btnEntrarLobby.setOnClickListener {
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainer, Sala_PagamentoFragment.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
        }

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