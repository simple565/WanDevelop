package com.maureen.wandevelop.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.maureen.wandevelop.R
import com.maureen.wandevelop.viewmodels.DiscoveryViewModel

class DiscoveryFragment : Fragment() {

    private lateinit var discoveryViewModel: DiscoveryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        discoveryViewModel = ViewModelProvider(this).get(DiscoveryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_discovery, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        discoveryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy: DiscoveryFragment")
    }
}