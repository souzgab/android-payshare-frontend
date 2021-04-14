package com.payshare.payshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.apiConnection.Conexao
import com.apiConnection.models.dataClassAdapter.CadastroData
import com.apiConnection.models.response.CadastroResponse
import com.apiConnection.models.response.LoginResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_cadastro.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class CadastroActivity : AppCompatActivity() {
    private val cadastroApi = Conexao.cadastroApi()
    private var responseCadastro: CadastroResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
    }

    private fun redirectLogin(){
        startActivity(
            Intent(this, LoginActivity::class.java)
        )
    }

    fun cadastrarUsuario(view: View) {
        val senha: EditText = findViewById(R.id.edit_text_senha)
        val confirmaSenha: EditText = findViewById(R.id.edit_text_confirma_senha)

       var name: EditText = findViewById(R.id.edit_text_nome)
       var age: Number = 21
       var address: String = "Empty Adress"
       var city: String = "Empty Adress"
       var cep: String = "08490560"
       var state: String = "Empty Adress"
       var email: EditText = findViewById(R.id.edit_text_email)
       var password: EditText = senha
       var cpf: EditText = findViewById(R.id.edit_text_cpf)
       var rg: String = "999999999"

       val user = CadastroData(
           name.text.toString(),
           age,
           address,
           city,
           cep,
           state,
           email.text.toString(),
           password.text.toString(),
           cpf.text.toString(),
           rg
       )
        Log.println(Log.INFO, "user", "log ".plus(user.toString()))
        if (validateUser(user)) {
            Log.println(Log.INFO, "user", "log ".plus("entrei"))
            cadastroApi.postCadastro(user).enqueue(object : Callback<CadastroResponse> {
                override fun onResponse(
                    call: Call<CadastroResponse>,
                    response: Response<CadastroResponse>
                ) {
                    Log.println(Log.INFO, "user", "log ".plus(response.toString()))
                    val data = response.body()
                    if (data != null) {
                        responseCadastro = data
                        Toast.makeText(baseContext, "Cadastro efetuado com sucesso, efetue seu Login!", Toast.LENGTH_SHORT).show()
                        redirectLogin()
                    }
                }

                override fun onFailure(call: Call<CadastroResponse>, t: Throwable) {
                    Log.e("Erro", "erro " + t.message)
                    Toast.makeText(baseContext, "Erro", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Falha ao cadastrar", Toast.LENGTH_SHORT).show()
        }
    }


    private fun validateUser(user: CadastroData) : Boolean {
        return user.email.isNotEmpty() && user.password.isNotEmpty()
    }

}