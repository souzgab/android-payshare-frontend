package com.shared.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.apiConnection.dataClassAdapter.user.UserData
import com.payshare.payshareapp.R
import com.payshare.payshareapp.Sala_PagamentoFragment
import com.shared.holders.HolderSala
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AdapterSala constructor() : RecyclerView.Adapter<HolderSala>() {
    lateinit var modelo: List<UserData>
    lateinit var context: Sala_PagamentoFragment

    constructor(context: Sala_PagamentoFragment): this() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderSala {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_card_sala_pagamento, parent, false)
        return HolderSala(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HolderSala, position: Int) {
        // 3 adicionar o holder instanciado na outra classe
        val dec = DecimalFormat("#,###.00")
        holder.nameUser.text = (modelo[position].name)
        holder.userAmountLobby.text = if ((modelo[position].userAmountLobby) == 0.00) "R$ 0,00" else "R$ ${dec.format((modelo[position]).userAmountLobby)}"
        holder.statusPagamento.text = if ((modelo[position].userAmountLobby) == 0.00) "Pago" else "Pendente"
    }

    override fun getItemCount(): Int {
        return if (modelo.isEmpty()) 0 else modelo.size
    }

    fun addTransaction(trx: List<UserData>) {
        this.modelo = trx
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseStringToDate(date:String) : String{
        val dateParse = LocalDateTime.parse(date)
        val format = dateParse.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        return format;
    }
}