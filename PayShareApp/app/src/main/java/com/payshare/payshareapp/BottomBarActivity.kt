package com.payshare.payshareapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.gson.Gson
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_bottom_bar.*

class BottomBarActivity : AppCompatActivity() {

    lateinit var  preferencias: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_bar)

        preferencias = getSharedPreferences("Auth", MODE_PRIVATE)
        var lobbySession = preferencias.getString("idLobby", null)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        bottomNavigation.add(MeowBottomNavigation.Model(0, R.drawable.ic_home))
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.extrato))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_logo))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.ic_carteira))
        bottomNavigation.add(MeowBottomNavigation.Model(4, R.drawable.ic_perfil))


        if (lobbySession != null) {
            bottomNavigation.show(2, true)
            mudarTela(Sala_PagamentoFragment.newInstance())
            supportActionBar?.hide()
        } else {
            bottomNavigation.show(0, true)
            mudarTela(HubFragment.newInstance())
        }

        bottomNavigation.setOnClickMenuListener {
            supportActionBar?.show()
            preferencias = getSharedPreferences("Auth", MODE_PRIVATE)
            lobbySession = preferencias.getString("idLobby", null)

            when(it.id){
                0 -> {
                    mudarTela(HubFragment.newInstance())
                }
                1 -> {
                    mudarTela(ExtratoFragment.newInstance())
                }
                2 -> {
                    if (lobbySession != null) {
                        Log.e("Sucesso", "Lobby não esta nula$lobbySession")
                        supportActionBar?.hide()
                        mudarTela(Sala_PagamentoFragment.newInstance())
                    } else {
                        Log.e("Sucesso", "Lobby esta nula$lobbySession")
                        mudarTela(CriarLobbyFragment.newInstance())
                    }
                }
                3 -> {
                    mudarTela(WalletFragment.newInstance())
                }
                4 -> {
                    mudarTela(PerfilFragment.newInstance())
                }

                else -> {
                    mudarTela(HubFragment.newInstance())
                }
            }
        }

    }

    private fun mudarTela(fragment: Fragment) {
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragmentContainer, fragment)
            .addToBackStack(Fragment::class.java.simpleName).commit()
    }

}

