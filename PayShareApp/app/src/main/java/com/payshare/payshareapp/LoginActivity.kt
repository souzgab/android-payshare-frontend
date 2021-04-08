package com.payshare.payshareapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var  preferencias: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preferencias = getSharedPreferences("Auth", MODE_PRIVATE)
        val ultimoUsuario = preferencias.getString("idUser", null)

        if(ultimoUsuario != null) {
            redirectLobby(ultimoUsuario)
        }
        btn_entrar.setOnClickListener {

            TelaHub()

        }

    }

    private fun TelaHub(){

        val i = Intent(this, BottomBarActivity::class.java)
        startActivity(i)
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
}