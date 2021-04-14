package com.payshare.payshareapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_bottomsheet.*


class BottomSheetFragment : Fragment() {

    lateinit var  preferencias: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_inserir.setOnClickListener{
            Toast.makeText(context, "Dinhiero adicionado", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
      @JvmStatic
      fun newInstance() =
            BottomSheetFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}