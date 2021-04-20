package com.payshare.payshareapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_adicionar_dinheiro.*


class CriarLobbyFragment : Fragment() {

    lateinit var  preferencias: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_criar_lobby, container, false)
    }



    companion object {
      @JvmStatic
      fun newInstance() =
            CriarLobbyFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}