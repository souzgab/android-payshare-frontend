package com.payshare.payshareapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_bottom_bar.*

class BottomBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_bar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        bottomNavigation.show(0)
        bottomNavigation.add(MeowBottomNavigation.Model(0, R.drawable.ic_home))
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_carteira))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_cartao))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.ic_perfil))

        mudarTela(HubFragment.newInstance())

        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                0 -> {
                    mudarTela(HubFragment.newInstance())
                }
                1 -> {
                    mudarTela(ExtratoFragment.newInstance())
                }
                2 -> {
                    mudarTela(WalletFragment.newInstance())
                }
                3 -> {
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
