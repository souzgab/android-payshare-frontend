package com.payshare.payshareapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apiConnection.Conexao
import com.apiConnection.dataClassAdapter.transactions.TransactionData
import com.apiConnection.models.response.user.UserResponse
import com.shared.adapters.AdapterExtrato
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExtratoFragment : Fragment() {

    lateinit var preferencias: SharedPreferences
    private val findUserById = Conexao.findUserById()
    lateinit var rvDinamic: RecyclerView
    lateinit var extratoAdapter: AdapterExtrato
    lateinit var idUser: String
    lateinit var token: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as BottomBarActivity).changeTitulo(1)
        val view : View = inflater.inflate(R.layout.fragment_extrato, container, false)


        //============= recuperando dados amarzenado em cache ========================
        preferencias =
            this.requireActivity().getSharedPreferences("Auth", Context.MODE_PRIVATE)
        idUser = preferencias.getString("idUser", null)!!
        token = preferencias.getString("Auth", null)!!
        var nameUser = preferencias.getString("nameUser", null)
        var moneyShared = preferencias.getFloat("userAmount", 0.00F)
        // ============================================================================

        // ================== caso tenha valor e nome em cache  =======================
        var saldoContaShared: TextView = view.findViewById(R.id.txt_valor_saldo)
        saldoContaShared.hint = "R$ ${moneyShared.toString()}"
        
        // ============================================================================

        //============= recuperando recycleView ========================
        rvDinamic = view.findViewById(R.id.rv_extrato)
        var layout = LinearLayoutManager(this.context)
        layout.orientation = RecyclerView.VERTICAL
        rvDinamic.layoutManager = layout
        listExtrato()
        extratoAdapter = AdapterExtrato(this)
        extratoAdapter.notifyDataSetChanged()
        //========================================================

        return view
    }

    fun listExtrato(){
        findUserById.findUserById(this.idUser.toInt(),"Bearer $token").enqueue(object :
            Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                var data = response.body()
                if (data != null) {
                    extratoAdapter.addTransaction(data.transactions)
                }
                rvDinamic.adapter = extratoAdapter
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    companion object {
      @JvmStatic
      fun newInstance() =
            ExtratoFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}