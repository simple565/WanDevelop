package com.maureen.wandevelop.base.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maureen.wandevelop.databinding.ItemFooterBinding
import com.maureen.wandevelop.network.NoMoreException
import com.maureen.wandevelop.network.parse

/**
 * @author lianml
 * @date 2024/5/23
 */
class LoadStateFooterAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadStateFooterAdapter.LoadStateFooterViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateFooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateFooterViewHolder {
        return LoadStateFooterViewHolder.create(parent, retry)
    }

    class LoadStateFooterViewHolder(private val binding: ItemFooterBinding, retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.tvRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.tvRetry.isVisible = false
            binding.tvErrorMsg.isVisible = false
            if (loadState is LoadState.Error) {
                binding.tvErrorMsg.text = loadState.error.parse().msg
                binding.tvErrorMsg.isVisible = true
                binding.tvRetry.isVisible = loadState.error !is NoMoreException
            }
            binding.pbLoading.isVisible = loadState is LoadState.Loading
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): LoadStateFooterViewHolder {
                val binding = ItemFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return LoadStateFooterViewHolder(binding, retry)
            }
        }
    }
}