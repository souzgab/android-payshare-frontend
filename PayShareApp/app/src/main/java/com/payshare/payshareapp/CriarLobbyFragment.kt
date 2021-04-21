package com.payshare.payshareapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import com.apiConnection.Conexao
import com.apiConnection.dataClassAdapter.lobby.LobbyData
import com.apiConnection.dataClassAdapter.lobby.LobbyDataBody
import com.apiConnection.dataClassAdapter.user.LoginData
import com.apiConnection.models.response.lobby.LobbyResponse
import com.apiConnection.models.response.user.LoginResponse
import com.apiConnection.models.response.user.UserResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_adicionar_dinheiro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CriarLobbyFragment : Fragment() {

    private val lobbyConnection = Conexao.createLobby()
    lateinit var preferencias: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_criar_lobby, container, false)

        val btnInserirDinheiro: Button = view.findViewById(R.id.btn_inserir)
        preferencias =
            this.activity!!.getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val idUser = preferencias.getString("idUser", null)
        val token = preferencias.getString("Auth", null)

        btnInserirDinheiro.setOnClickListener {

            val txtDescricao: EditText = view.findViewById(R.id.edit_nome_lobby)
            val txtDescricaoPedido: EditText = view.findViewById(R.id.edit_descricao_pedido)
            val txtValorLobby: EditText = view.findViewById(R.id.edit_text_valor_lobby)

            val txtDesc = txtDescricao.text.toString()
            val txtDescPedido = txtDescricaoPedido.text.toString()
            val txtValPedido =
                if (txtValorLobby.text.isBlank()) 0.0 else txtValorLobby.text.toString().toDouble()

            Log.e("Sucesso", "desc" + txtDesc)
            Log.e("Sucesso", "txtDescPedido" + txtDescPedido)
            Log.e("Sucesso", "txtValPedido" + txtValPedido)
            if (validationEmptyLobby(
                    txtDescricao,
                    txtDescricaoPedido,
                    txtValorLobby
                )
            ) {
                val lobbyData = LobbyDataBody(
                    txtDesc,
                    txtDescPedido,
                    txtValPedido,
                    0.00
                )

                idUser?.toInt()?.let {
                    lobbyConnection.createLobby(
                        lobbyData,
                        it,
                        "Bearer " + token.toString()

                    ).enqueue(object : Callback<LobbyResponse> {
                        override fun onResponse(
                            call: Call<LobbyResponse>,
                            response: Response<LobbyResponse>
                        ) {
                            val data = response.body()
                            if (data != null) {
                                val editor = preferencias.edit()
                                editor.putString("idLobby", data.id.toString())
                                editor.apply()
                                val transaction: FragmentTransaction =
                                    fragmentManager!!.beginTransaction()
                                transaction.replace(
                                    R.id.fragmentContainer,
                                    Sala_PagamentoFragment.newInstance()
                                )
                                transaction.addToBackStack(null)
                                transaction.commit()
                                Log.e("Sucesso", "Lobby" + Gson().toJson(data))
                            } else {
                                Toast.makeText(context, "Falha ao criar lobby", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<LobbyResponse>, t: Throwable) {
                            Log.e("Erro", "erro " + t.message)
                        }
                    })
                }

            } else {
                Toast.makeText(context, "Verifique os campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun validationEmptyLobby(
        descricao: EditText,
        descricaoPedido: EditText,
        valorPedido: EditText
    ): Boolean {
        return descricao.text.isNotEmpty() && descricaoPedido.text.isNotEmpty() && valorPedido.text.isNotEmpty()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CriarLobbyFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}