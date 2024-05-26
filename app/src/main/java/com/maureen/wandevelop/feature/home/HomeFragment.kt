package com.maureen.wandevelop.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.maureen.wandevelop.R
import com.maureen.wandevelop.databinding.FragmentHomeBinding
import com.maureen.wandevelop.ext.startActivity
import com.maureen.wandevelop.main.MainViewModel
import com.maureen.wandevelop.feature.discovery.SearchActivity
import kotlinx.coroutines.flow.onEach

class HomeFragment : Fragment() {
    companion object {
        private const val TAG = "HomeFragment"
    }

    private val viewModel: HomeViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()

    private lateinit var viewBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    private fun initView() {
        viewBinding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_search) {
                requireContext().startActivity(SearchActivity::class.java)
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun observeData() {
        activityViewModel.uiState.onEach {

        }
    }
}