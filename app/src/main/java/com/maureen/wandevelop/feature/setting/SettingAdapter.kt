package com.maureen.wandevelop.feature.setting

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.drakeet.multitype.MultiTypeAdapter
import com.maureen.wandevelop.R
import com.maureen.wandevelop.databinding.ItemSettingActionBinding
import com.maureen.wandevelop.databinding.ItemSettingEmptyBinding
import com.maureen.wandevelop.databinding.ItemSettingSwitchBinding
import com.maureen.wandevelop.entity.SettingItem
import com.maureen.wandevelop.entity.SettingType

/**
 * 设置列表适配器
 * @author lianml
 * @date 2023/12/26
 */
class SettingAdapter : MultiTypeAdapter() {

    init {
        register(SettingItem::class).to(
            EmptyProvider(),
            SwitchProvider(),
            ActionProvider()
        ).withKotlinClassLinker { _, data ->
            when (data.type) {
                SettingType.EMPTY -> EmptyProvider::class
                SettingType.SWITCH -> SwitchProvider::class
                else -> ActionProvider::class
            }
        }
    }

    fun findItemByName(name: String): Pair<Int, SettingItem?> {
        this.items.forEachIndexed { index, any ->
            if (name == (any as? SettingItem)?.name) {
                return Pair(index, any)
            }
        }
        return Pair(-1, null)
    }

    class EmptyProvider : ItemViewDelegate<SettingItem, EmptyProvider.ViewHolder>() {

        class ViewHolder(viewBinding: ItemSettingEmptyBinding) :
            RecyclerView.ViewHolder(viewBinding.root)

        override fun onBindViewHolder(holder: ViewHolder, item: SettingItem) {
        }

        override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
            return ViewHolder(
                ItemSettingEmptyBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
        }
    }

    class SwitchProvider : ItemViewDelegate<SettingItem, SwitchProvider.ViewHolder>() {

        class ViewHolder(private val viewBinding: ItemSettingSwitchBinding) :
            RecyclerView.ViewHolder(viewBinding.root) {
            var data: SettingItem? = null

            init {
                viewBinding.swt.setOnClickListener {
                    data?.action?.invoke()
                }
            }

            fun bind(item: SettingItem) {
                this.data = item
                viewBinding.ivIcon.isVisible = null != item.icon
                viewBinding.tvName.text = item.name
                viewBinding.swt.isChecked = item.value?.toBooleanStrictOrNull() ?: false
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, item: SettingItem) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
            return ViewHolder(
                ItemSettingSwitchBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
        }
    }

    class ActionProvider : ItemViewDelegate<SettingItem, ActionProvider.ViewHolder>() {
        class ViewHolder(private val viewBinding: ItemSettingActionBinding) :
            RecyclerView.ViewHolder(viewBinding.root) {
            var data: SettingItem? = null

            init {
                viewBinding.root.setOnClickListener { data?.action?.invoke() }
            }

            fun bind(item: SettingItem) {
                this.data = item
                val context = viewBinding.root.context
                viewBinding.ivIcon.isVisible = null != item.icon
                viewBinding.tvName.text = item.name
                viewBinding.tvValue.text = item.value
                viewBinding.ivRoute.isVisible = SettingType.ROUTE == item.type
                viewBinding.tvName.setTextColor(
                    if (item.warn) ContextCompat.getColor(context, R.color.red_500) else viewBinding.tvName.textColors.defaultColor
                )
                viewBinding.tvName.gravity = if (item.warn) Gravity.CENTER else Gravity.START
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, item: SettingItem) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            context: Context,
            parent: ViewGroup
        ): ViewHolder {
            return ViewHolder(
                ItemSettingActionBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
                )
            )
        }
    }
}