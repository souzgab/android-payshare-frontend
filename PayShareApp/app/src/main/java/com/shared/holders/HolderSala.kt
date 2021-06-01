package com.shared.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.payshare.payshareapp.R

class HolderSala : RecyclerView.ViewHolder {

    // 1º Criar var do tipo do component
    var nameUser: TextView
    var userAmountLobby : TextView
    var statusPagamento : TextView

    constructor(ItemView: View) : super(ItemView) {

        this.nameUser = ItemView.findViewById(R.id.nome_user_pagamento)
        this.userAmountLobby = ItemView.findViewById(R.id.valor_pagar)
        this.statusPagamento = ItemView.findViewById(R.id.status_pagamento)
        // 2 instanciar aqui a var
    }
}