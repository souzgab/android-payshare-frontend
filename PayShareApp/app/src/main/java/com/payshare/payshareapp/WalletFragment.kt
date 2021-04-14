package com.payshare.payshareapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_bottomsheet.*


class WalletFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)


    }



    companion object {
      @JvmStatic
      fun newInstance() =
            WalletFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}