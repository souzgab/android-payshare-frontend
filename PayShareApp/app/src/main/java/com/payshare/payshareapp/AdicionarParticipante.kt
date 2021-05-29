package com.payshare.payshareapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.apiConnection.Conexao
import com.apiConnection.models.response.lobby.LobbyResponse
import com.apiConnection.models.response.transaction.TransactionWalletResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdicionarParticipante : Fragment() {

    private val lobbyConnection = Conexao.findByLobbyUser()
    lateinit var preferencias: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_entrar_lobby, container, false)
        preferencias =
            this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val idUser = preferencias.getString("idUser", null)
        val token = preferencias.getString("Auth", null)
        var nameUser = preferencias.getString("nameUser", null)
        var moneyShared = preferencias.getFloat("userAmount", 0.00F)
        var idLobby = preferencias.getString("idLobby", null)

        val btnAddParticipante: Button = view.findViewById(R.id.btn_participar)

        btnAddParticipante.setOnClickListener {
            val txtIdLobby: EditText = view.findViewById(R.id.id_lobby)
            val txtValidaIdLobby =
                if (txtIdLobby.text.isBlank()) 0 else txtIdLobby.text.toString().toInt()

            if (validaValor(txtIdLobby) && txtValidaIdLobby != 0) {
                //========= Busca informações da lobby do usuario ======================================
                lobbyConnection.findLobbyById(txtValidaIdLobby, "Bearer " + token.toString())
                    .enqueue(object : Callback<LobbyResponse> {
                        override fun onResponse(
                            call: Call<LobbyResponse>,
                            response: Response<LobbyResponse>
                        ) {
                            var data = response.body()
                            Log.e("Sucesso", "Lobyyyyy aquiiiiii topppp" + Gson().toJson(data))
                            if (data != null) {

                                val builder = AlertDialog.Builder(view.context)
                                // set title
                                builder.setTitle("Adição de valor na carteira")

                                //set content area
                                builder.setMessage("Tem certeza que deseja entrar na sala ${data.lobbyDescription} ?")

                                //set negative button
                                builder.setPositiveButton(
                                    "Confirmar"
                                ) { dialog, id ->
                                    // User clicked Update Now button

                                    idUser?.toInt()?.let {
                                        lobbyConnection.addUserLobby(
                                            data.id,
                                            it,
                                            "Bearer " + token.toString()
                                        )
                                            .enqueue(object :
                                                Callback<LobbyResponse> {
                                                override fun onResponse(
                                                    call: Call<LobbyResponse>,
                                                    response: Response<LobbyResponse>
                                                ) {

                                                    var data = response.body()

                                                    if (data != null) {
                                                        val editor = preferencias.edit()
                                                        editor.putString(
                                                            "idLobby",
                                                            data.id.toString()
                                                        )
                                                        editor.apply()

                                                        val transaction: FragmentTransaction =
                                                            requireFragmentManager().beginTransaction()
                                                        transaction.replace(
                                                            R.id.fragmentContainer,
                                                            Sala_PagamentoFragment.newInstance()
                                                        )
                                                        transaction.addToBackStack(null)
                                                        transaction.commit()
                                                        (activity as BottomBarActivity).changeIcon(2)
                                                    }

                                                }

                                                override fun onFailure(
                                                    call: Call<LobbyResponse>,
                                                    t: Throwable
                                                ) {
                                                    Log.e("Erro", "erro " + t.message)
                                                }
                                            })
                                    }

                                }

                                //set positive button
                                builder.setNegativeButton(
                                    "Cancelar"
                                ) { dialog, id ->
                                    // User cancelled the dialog
                                }

                                builder.show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Não foi possivel localizar a lobby informada",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<LobbyResponse>, t: Throwable) {
                            Log.e("Erro", "erro " + t.message)
                        }
                    })
            } else {
                Toast.makeText(
                    context,
                    "Numero de identificação não pode estar em branco",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
    }

    private fun validaValor(valor: EditText): Boolean {
        return valor.text.isNotEmpty()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AdicionarParticipante().apply {
                arguments = Bundle().apply {}
            }
    }
}