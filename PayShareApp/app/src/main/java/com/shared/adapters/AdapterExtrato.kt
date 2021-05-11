package com.shared.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apiConnection.dataClassAdapter.transactions.TransactionData
import com.payshare.payshareapp.ExtratoFragment
import com.payshare.payshareapp.R
import com.shared.holders.HolderExtrato

class AdapterExtrato constructor() : RecyclerView.Adapter<HolderExtrato>() {
    lateinit var modelo: List<TransactionData>
    lateinit var context: ExtratoFragment

    constructor(context: ExtratoFragment): this() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderExtrato {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.extrato_modelo, parent, false)
        return HolderExtrato(view)
    }

    override fun onBindViewHolder(holder: HolderExtrato, position: Int) {
        // 3 adicionar o holder instanciado na outra classe
        holder.dataPagamento.text = modelo[position].expirationDate
        holder.description.text = (modelo[position].description)
    }

    override fun getItemCount(): Int {
        return modelo.size
    }

    fun addTransaction(trx: List<TransactionData>) {
        this.modelo = trx
    }
}