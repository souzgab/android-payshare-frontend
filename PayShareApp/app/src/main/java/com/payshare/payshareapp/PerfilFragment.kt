package com.payshare.payshareapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.apiConnection.models.response.user.UserResponse
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PerfilFragment : Fragment() {

    lateinit var preferencias: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_perfil, container, false)
        preferencias =
            this.activity!!.getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val idUser = preferencias.getString("idUser", null)
        val token = preferencias.getString("Auth", null)
        var nameUser = preferencias.getString("nameUser", null)
        var emailUser = preferencias.getString("emailUser", null)

        val email : TextView = view.findViewById(R.id.txt_dado_email)
        val nome : TextView = view.findViewById(R.id.txt_dado_nome)

        email.hint = "$emailUser"
        nome.hint = "$nameUser"

        // Realiza o logout do usuario da sessão
        val btnSair : Button = view.findViewById(R.id.btn_sair)
        btnSair.setOnClickListener(View.OnClickListener {
            val editor = preferencias.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        })

        return view
    }

    companion object {
      @JvmStatic
        fun newInstance() =
            PerfilFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}