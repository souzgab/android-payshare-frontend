package com.payshare.payshareapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class LobbyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lobby, container, false)
    }

    companion object {
      @JvmStatic
      fun newInstance() =
            LobbyFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}