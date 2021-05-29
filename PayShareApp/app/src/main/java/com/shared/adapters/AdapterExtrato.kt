package com.shared.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.apiConnection.dataClassAdapter.transactions.TransactionData
import com.payshare.payshareapp.ExtratoFragment
import com.payshare.payshareapp.R
import com.shared.holders.HolderExtrato
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HolderExtrato, position: Int) {
        // 3 adicionar o holder instanciado na outra classe

        holder.dataPagamento.text = parseStringToDate((modelo[position].expirationDate))
        holder.descExtract.text = (modelo[position].description)
        holder.valorExtract.text = (modelo[position].amount.toString())
    }

    override fun getItemCount(): Int {
        return modelo.size
    }

    fun addTransaction(trx: List<TransactionData>) {
        this.modelo = trx
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseStringToDate(date:String) : String{
        val dateParse = LocalDateTime.parse(date)
        val format = dateParse.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        return format;
    }
}