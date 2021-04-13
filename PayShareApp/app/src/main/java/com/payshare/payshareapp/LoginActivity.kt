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
import com.apiConnection.models.dataClassAdapter.LoginPost
import com.apiConnection.models.response.LoginResponse
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
            TelaHub(ultimoUsuario)
        }

    }

    fun entrar(view: View) {
        val etLogin: EditText = findViewById(R.id.edit_text_email)
        val etSenha: EditText = findViewById(R.id.edit_text_senha)
        val email = etLogin.text.toString()
        val password = etSenha.text.toString()

        if(loginValidation(email, etSenha)){

            loginApi.postLogin(
                LoginPost(email, password)
            ).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    val data = response.body()
                    if (data != null) {
                        val editor = preferencias.edit()
                        editor.putString("Auth", data.token)
                        editor.putString("emailUser", data.email)
                        editor.putString("idUser", data.id.toString())
                        editor.putString("nameUser", data.name)
                        editor.commit()
                        val ultimoUsuario = preferencias.getString("idUser", null)
                        if(ultimoUsuario != null) {
                            TelaHub(ultimoUsuario)
                        }
                        Log.println(Log.INFO, "login", "log qlq coisa ae".plus(preferencias.toString()))
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

    fun redirectLobby(user: String) {
        // Trocar Activity abaixo para a tela certa
        startActivity(
            Intent(this, CadastroActivity::class.java)
                .putExtra("nameUser", user)
        )
    }

    private fun TelaHub(user:String){

        val i = Intent(this, BottomBarActivity::class.java)
        startActivity(i)

    }
}