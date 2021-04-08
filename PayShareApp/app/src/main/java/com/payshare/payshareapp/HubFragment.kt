package com.payshare.payshareapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class HubFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hub, container, false)
    }

    companion object {
      @JvmStatic
      fun newInstance() =
            HubFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}