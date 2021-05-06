package com.payshare.payshareapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.apiConnection.Conexao
import com.apiConnection.dataClassAdapter.user.CadastroData
import com.apiConnection.models.response.user.CadastroResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroActivity : AppCompatActivity() {
    private val cadastroApi = Conexao.cadastroApi()
    private var responseCadastro: CadastroResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        var id: Int = resources.getIdentifier("bg_button_clear_red", "drawable", packageName)

        findViewById<Button>(R.id.btn_cadastro).isClickable = false
        findViewById<Button>(R.id.btn_cadastro).background = resources.getDrawable(id)
        findViewById<Button>(R.id.btn_cadastro).text = "Concorde Termos"
        findViewById<Button>(R.id.btn_cadastro).setTextColor(Color.parseColor("#f0f0f0"))
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
        if (validateUser(user)) {
            cadastroApi.postCadastro(user).enqueue(object : Callback<CadastroResponse> {
                override fun onResponse(
                    call: Call<CadastroResponse>,
                    response: Response<CadastroResponse>
                ) {
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
        var valid = true
        val nome = findViewById<EditText>(R.id.edit_text_nome)
        val cpf = findViewById<EditText>(R.id.edit_text_cpf)
        val email = findViewById<EditText>(R.id.edit_text_email)
        val senha = findViewById<EditText>(R.id.edit_text_senha)
        val confirmaSenha = findViewById<EditText>(R.id.edit_text_confirma_senha)

        when {
            nome.text.isEmpty() -> {
                valid = false
                nome.error = "Nome precisa ser preenchido!"
            }
            cpf.text.isEmpty() -> {
                valid = false
                cpf.error = "CPF precisa ser preenchido!"
            }
            email.text.isEmpty() -> {
                valid = false
                email.error = "E-mail precisa ser preenchido!"
            }
            senha.text.isEmpty() -> {
                valid = false
                senha.error = "Senha precisa ser preenchida!"
            }
            confirmaSenha.text.isEmpty() -> {
                valid = false
                confirmaSenha.error = "Confirme sua senha!"
            }
            senha.text.toString() != confirmaSenha.text.toString() -> {
                valid = false
                confirmaSenha.error = "Suas senhas estão diferentes!"
            }
            senha.text.length < 6 -> {
                valid = false
                senha.error = "Sua senha possui menos de 6 Caracteres"
            }
        }
        return valid
    }

    fun isAgree(view: View) {
        if (findViewById<CheckBox>(R.id.ch_cadastro).isChecked) run {
            var id: Int = resources.getIdentifier("bg_button_normal", "drawable", packageName)
            findViewById<Button>(R.id.btn_cadastro).isClickable = true
            findViewById<Button>(R.id.btn_cadastro).background = resources.getDrawable(id)
            findViewById<Button>(R.id.btn_cadastro).text = "Cadastrar"
            findViewById<Button>(R.id.btn_cadastro).setTextColor(Color.parseColor("#f0f0f0"))
        } else {
            var id: Int = resources.getIdentifier("bg_button_clear_red", "drawable", packageName)
            findViewById<Button>(R.id.btn_cadastro).isClickable = false
            findViewById<Button>(R.id.btn_cadastro).background = resources.getDrawable(id)
            findViewById<Button>(R.id.btn_cadastro).text = "Concorde Termos"
            findViewById<Button>(R.id.btn_cadastro).setTextColor(Color.parseColor("#f0f0f0"))
        }
    }
}