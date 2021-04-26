package com.payshare.payshareapp

import android.app.AlertDialog
import android.content.Context
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
import com.apiConnection.dataClassAdapter.lobby.LobbyDataBody
import com.apiConnection.models.response.lobby.LobbyResponse
import com.apiConnection.models.response.transaction.TransactionResponse
import com.apiConnection.models.response.user.UserResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransferenciaEntreContas : Fragment() {

    private val connection = Conexao.findUserById()
    private val transaction = Conexao.createTransactionWallet()
    lateinit var preferencias: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_transferencia, container, false)
        val btnTransferir: Button = view.findViewById(R.id.btn_inserir)

        btnTransferir.setOnClickListener {
            preferencias =
                this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
            val idUser = preferencias.getString("idUser", null)
            val token = preferencias.getString("Auth", null)

            val cpfFavorecido: EditText = view.findViewById(R.id.cpf_favorecido)
            val valorTransferencia: EditText = view.findViewById(R.id.edit_text_valor_transfer)

            val valorTransferenciaConverter  = if (valorTransferencia.text.isBlank()) 0.0 else valorTransferencia.text.toString().toDouble()

            //========= Busca informações do usuario ======================================

            if (validaBody(cpfFavorecido, valorTransferencia) && valorTransferenciaConverter != 0.00) {
                connection.findUserByCpf(
                    cpfFavorecido.text.toString(),
                    "Bearer " + token.toString()
                )
                    .enqueue(object : Callback<UserResponse> {
                        override fun onResponse(
                            call: Call<UserResponse>,
                            response: Response<UserResponse>
                        ) {
                            var data = response.body()

                            if (data != null) {

                                if (data.userId.toString().equals(idUser)) {
                                    Toast.makeText(
                                        context,
                                        "Você não pode realizar transferência para sí mesmo",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {

                                    val editor = preferencias.edit()
                                    //Instantiate builder variable
                                    val builder = AlertDialog.Builder(view.context)

                                    // set title
                                    builder.setTitle("Transfêrencia entre contas")

                                    //set content area
                                    builder.setMessage("Tem certeza que deseja enviar para ${data.name} R$ ${valorTransferencia.text.toString()}")

                                    //set negative button
                                    builder.setPositiveButton(
                                        "Confirmar"
                                    ) { dialog, id ->

                                        idUser?.toInt()?.let {
                                            transaction.transferAccounts(
                                                it,
                                                valorTransferenciaConverter,
                                                data.cpf,
                                                "Bearer " + token.toString()

                                            ).enqueue(object : Callback<TransactionResponse> {
                                                override fun onResponse(
                                                    call: Call<TransactionResponse>,
                                                    response: Response<TransactionResponse>
                                                ) {
                                                    val data = response.body()
                                                    if (data != null) {
                                                        Toast.makeText(
                                                            context,
                                                            "Transferência realizada com sucesso!",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                            .show()
                                                        val transaction: FragmentTransaction =
                                                            fragmentManager!!.beginTransaction()
                                                        transaction.replace(
                                                            R.id.fragmentContainer,
                                                            HubFragment.newInstance()
                                                        )
                                                        transaction.addToBackStack(null)
                                                        transaction.commit()
                                                        Log.e(
                                                            "Sucesso",
                                                            "transaction" + Gson().toJson(data)
                                                        )
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Falha ao realizar transferência !!",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                            .show()
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<TransactionResponse>,
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
                                    Log.e("Sucesso", "Usuario" + Gson().toJson(data))
                                }

                            } else {
                                Toast.makeText(
                                    context,
                                    "Não foi possivel localizar o beneficiário",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                        }

                        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                            Log.e("Erro", "erro " + t.message)
                        }
                    })

            } else {
                Toast.makeText(context, "Verifique os campos", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun validaBody(cpf: EditText, valorTransferencia: EditText): Boolean {
        return cpf.text.isNotEmpty() && valorTransferencia.text.isNotEmpty()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TransferenciaEntreContas().apply {
                arguments = Bundle().apply {}
            }
    }
}