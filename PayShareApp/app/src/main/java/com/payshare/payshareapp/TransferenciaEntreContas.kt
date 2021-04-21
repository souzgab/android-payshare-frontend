package com.payshare.payshareapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class TransferenciaEntreContas : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_transferencia, container, false)

        //Instantiate builder variable
        val builder = AlertDialog.Builder(view.context)

        // set title
        builder.setTitle("Transfêrencia entre contas")

        //set content area
        builder.setMessage("Tem certeza que deseja enviar para Vinicius Alves R$ 500.00")

        //set negative button
        builder.setPositiveButton(
            "Confirmar") { dialog, id ->
            // User clicked Update Now button
            val transaction : FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.fragmentContainer, HubFragment.newInstance())
            transaction.addToBackStack(null)
            transaction.commit()
            Toast.makeText(context, "Updating your device",Toast.LENGTH_SHORT).show()
        }

        //set positive button
        builder.setNegativeButton(
            "Cancelar") { dialog, id ->
            // User cancelled the dialog
        }

        builder.show()

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TransferenciaEntreContas().apply {
                arguments = Bundle().apply {}
            }
    }
}