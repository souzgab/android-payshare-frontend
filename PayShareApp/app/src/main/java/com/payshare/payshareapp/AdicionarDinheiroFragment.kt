package com.payshare.payshareapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.apiConnection.Conexao
import com.apiConnection.models.response.transaction.TransactionWalletResponse
import com.apiConnection.models.response.user.UserResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_adicionar_dinheiro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdicionarDinheiroFragment : Fragment() {

    private val createTransactionWallet = Conexao.createTransactionWallet()
    lateinit var  preferencias: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_adicionar_dinheiro, container, false)
        preferencias =
            this.activity!!.getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val idUser = preferencias.getString("idUser", null)
        val token = preferencias.getString("Auth", null)

        val btnAdicionarDinheiro : Button = view.findViewById(R.id.btn_inserir)

        btnAdicionarDinheiro.setOnClickListener {
            val txtValor : EditText = view.findViewById(R.id.et_valor)

            val txtValWallet =
                if (txtValor.text.isBlank()) 0.0 else txtValor.text.toString().toDouble()

            if (validaValor(txtValor) && txtValWallet != 0.00) {
                //Instantiate builder variable
                val builder = AlertDialog.Builder(view.context)

                // set title
                builder.setTitle("Adição de valor na carteira")

                //set content area
                builder.setMessage("Tem certeza que deseja adicionar R$ ${txtValWallet.toString()} em sua carteira ?")

                //set negative button
                builder.setPositiveButton(
                    "Confirmar"
                ) { dialog, id ->
                    // User clicked Update Now button

                    idUser?.toInt()?.let {
                        createTransactionWallet.addMoneyInWallet(
                            it,
                            txtValWallet,
                            "Bearer " + token.toString()
                        )
                            .enqueue(object : Callback<TransactionWalletResponse> {
                                override fun onResponse(
                                    call: Call<TransactionWalletResponse>,
                                    response: Response<TransactionWalletResponse>
                                ) {
                                    var data = response.body()

                                    if (data != null) {
                                        val editor = preferencias.edit()
                                        editor.putString("initPoint", data.body.initPoint)
                                        editor.apply()
                                        Log.e("Sucesso", "Transaction wallet aquiii " + Gson().toJson(data))
                                        val intent =
                                            Intent(context, MercadoPagoCheckout::class.java)
                                        startActivity(intent)
                                    }

                                }

                                override fun onFailure(
                                    call: Call<TransactionWalletResponse>,
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
                Toast.makeText(context, "Valor não pode estar em branco", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun validaValor (valor : EditText) : Boolean{
        return valor.text.isNotEmpty()
    }

    companion object {
      @JvmStatic
      fun newInstance() =
            AdicionarDinheiroFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}