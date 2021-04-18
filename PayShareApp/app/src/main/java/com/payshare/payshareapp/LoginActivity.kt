package com.payshare.payshareapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.apiConnection.Conexao
import com.apiConnection.dataClassAdapter.user.LoginData
import com.apiConnection.models.response.user.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val loginApi = Conexao.loginApi()
    lateinit var  preferencias: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferencias = getSharedPreferences("Auth", MODE_PRIVATE)
        val ultimoUsuario = preferencias.getString("idUser", null)

        if(ultimoUsuario != null) {
            TelaHub()
        }

    }

    fun entrar(view: View) {
        val etLogin: EditText = findViewById(R.id.edit_text_email)
        val etSenha: EditText = findViewById(R.id.edit_text_senha)
        val email = etLogin.text.toString()
        val password = etSenha.text.toString()
        Log.println(Log.INFO, "login", "log ".plus(email + password))
        if(loginValidation(email, etSenha)){
            val loginDt =
                LoginData(
                    email,
                    password
                )

            loginApi.postLogin(
                loginDt
            ).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    Log.println(Log.INFO, "login", "log ".plus(response.body()))
                    val data = response.body()
                    if (data != null) {
                        val editor = preferencias.edit()
                        editor.putString("Auth", data.token)
                        editor.putString("emailUser", data.email)
                        editor.putString("idUser", data.id.toString())
                        editor.putString("nameUser", data.name)
                        editor.commit()
                        TelaHub()
                        Toast.makeText(baseContext, response.body().toString(), Toast.LENGTH_SHORT).show()
                        Log.println(Log.INFO, "login", "log qlq coisa ae".plus(preferencias.toString()))
                    }else {
                        Toast.makeText(baseContext, "Falha ao autenticar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("Erro", "erro " + t.message)
                    Toast.makeText(baseContext, "Erro", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Falha ao autenticar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginValidation(email: String, password: EditText): Boolean {
        return email.isNotEmpty() && password.text.isNotEmpty()
    }

    fun viewCadastro(view: View) {
        val intent = Intent(this, CadastroActivity::class.java)
        startActivity(intent)
    }

    private fun TelaHub(){
        startActivity(Intent(this, BottomBarActivity::class.java))
    }
}