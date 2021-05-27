package com.payshare.payshareapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.apiConnection.Conexao
import java.text.DecimalFormat


class ExtratoFragment : Fragment() {

    lateinit var preferencias: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view : View = inflater.inflate(R.layout.fragment_extrato, container, false)


        //============= recuperando dados amarzenado em cache ========================
        preferencias =
            this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        val idUser = preferencias.getString("idUser", null)
        val token = preferencias.getString("Auth", null)
        var nameUser = preferencias.getString("nameUser", null)
        var moneyShared = preferencias.getFloat("userAmount", 0.00F)
        // ============================================================================

        // ================== caso tenha valor e nome em cache  =======================
        var saldoContaShared: TextView = view.findViewById(R.id.txt_valor_saldo)
        val dec = DecimalFormat("#,###.00")
        saldoContaShared.hint = if (moneyShared.toString() == "0.00") "R$ 0.00" else  "R$ ${dec.format(moneyShared)}"
        
        // ============================================================================

        return view
    }

    companion object {
      @JvmStatic
      fun newInstance() =
            ExtratoFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}