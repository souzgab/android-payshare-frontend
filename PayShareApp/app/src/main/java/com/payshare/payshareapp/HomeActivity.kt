package com.payshare.payshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btn_cadastrar.setOnClickListener {


            TelaCadastro()

        }

        btn_login.setOnClickListener {

            TelaLogin()

        }
    }

    private fun TelaLogin() {

        val i = Intent(this@HomeActivity, LoginActivity::class.java)
        startActivity(i)

    }

    private fun TelaCadastro() {

        val i = Intent(this@HomeActivity, CadastroActivity::class.java)
        startActivity(i)

    }

}



