package com.maureen.wandevelop.feature.discovery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.maureen.wandevelop.R
import com.maureen.wandevelop.databinding.FragmentDiscoveryBinding

class DiscoveryFragment : Fragment() {
    companion object {
        private const val TAG = "DiscoveryFragment"
    }

    private val discoveryViewModel: DiscoveryViewModel by viewModels()
    private lateinit var viewBinding: FragmentDiscoveryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView: ")
        viewBinding = FragmentDiscoveryBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
    }
}