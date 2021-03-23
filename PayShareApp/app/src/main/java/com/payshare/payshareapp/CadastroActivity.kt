package com.payshare.payshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cadastro.*

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        btn_cadastro.setOnClickListener {

            TelaLogin()

        }
    }

    private fun TelaLogin(){

        val i = Intent(this@CadastroActivity, LoginActivity::class.java)
        startActivity(i)

    }


}