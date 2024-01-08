package com.maureen.wandevelop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maureen.wandevelop.databinding.ItemSettingActionBinding
import com.maureen.wandevelop.databinding.ItemSettingSwitchBinding
import com.maureen.wandevelop.databinding.ItemSettingTitleBinding
import com.maureen.wandevelop.entity.SettingItem
import com.maureen.wandevelop.entity.SettingType

/**
 * @author lianml
 * @date 2023/12/26
 */
class SettingAdapter : ListAdapter<SettingItem, RecyclerView.ViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SettingItem>() {
            override fun areItemsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
                return newItem == oldItem
            }

            override fun areContentsTheSame(oldItem: SettingItem, newItem: SettingItem): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SettingType.TITLE -> {
                TitleProvider(
                    ItemSettingTitleBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            }
            SettingType.SWITCH -> {
                SwitchProvider(
                    ItemSettingSwitchBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            }
            else -> {
                ActionProvider(
                    ItemSettingActionBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is TitleProvider -> {
                holder.bind(item)
            }

            is ActionProvider -> {
                holder.bind(item)
            }

            is SwitchProvider -> {
                holder.bind(item)
            }
        }
    }

    class TitleProvider(private val viewBinding: ItemSettingTitleBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        var data: SettingItem? = null
        fun bind(item: SettingItem) {
            this.data = item
            viewBinding.tvTitle.text = item.name
        }
    }

    class SwitchProvider(private val viewBinding: ItemSettingSwitchBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        var data: SettingItem? = null

        init {
            viewBinding.swt.setOnClickListener { data?.clickAction?.invoke(it) }
        }

        fun bind(item: SettingItem) {
            this.data = item
            viewBinding.ivIcon.setImageResource(item.iconResId)
            viewBinding.tvName.text = item.name
            viewBinding.swt.isChecked = item.value == true.toString()
        }
    }

    class ActionProvider(private val viewBinding: ItemSettingActionBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        var data: SettingItem? = null

        init {
            viewBinding.root.setOnClickListener { data?.clickAction?.invoke(it) }
        }

        fun bind(item: SettingItem) {
            this.data = item
            viewBinding.ivIcon.setImageResource(item.iconResId)
            viewBinding.tvName.text = item.name
            viewBinding.tvValue.text = item.value
            viewBinding.ivRoute.isVisible = SettingType.ROUTE == item.type
            viewBinding.tvValue.isVisible = SettingType.ROUTE != item.type
        }
    }
}