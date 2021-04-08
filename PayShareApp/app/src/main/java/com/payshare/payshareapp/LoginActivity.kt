package com.payshare.payshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_entrar.setOnClickListener {

            TelaHub()

        }

    }

    private fun TelaHub(){

        val i = Intent(this, BottomBarActivity::class.java)
        startActivity(i)

    }

    fun viewCadastro(view:View) {
        val intent = Intent(this, CadastroActivity::class.java)
        startActivity(intent)
    }


}