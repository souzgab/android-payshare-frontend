package com.shared.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apiConnection.dataClassAdapter.transactions.TransactionData
import com.payshare.payshareapp.R

class HolderExtrato : RecyclerView.ViewHolder{

    // 1º Criar var do tipo do component
    var dataPagamento: TextView
    var description: TextView
    var descExtract : TextView
    var valorExtract : TextView

    constructor(ItemView: View) : super(ItemView) {

        this.dataPagamento = ItemView.findViewById(R.id.tv_DataPagamento)
        this.description = ItemView.findViewById(R.id.tv_Description)
        this.descExtract = ItemView.findViewById(R.id.txt_desc_extrato)
        this.valorExtract = ItemView.findViewById(R.id.valor_gasto)
        // 2 instanciar aqui a var
    }


}