package com.maureen.wandevelop.feature.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maureen.wandevelop.databinding.ItemNotificationBinding
import com.maureen.wandevelop.entity.MessageInfo

/**
 * 通知消息列表适配器
 * @author lianml
 * @date 2024/5/4
 */
class NotificationAdapter: PagingDataAdapter<MessageInfo, NotificationAdapter.ViewHolder>(differCallback) {
    companion object {
        val differCallback = object : DiffUtil.ItemCallback<MessageInfo>() {
            override fun areItemsTheSame(oldItem: MessageInfo, newItem: MessageInfo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MessageInfo, newItem: MessageInfo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.also { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    class ViewHolder(private val viewBinding: ItemNotificationBinding): RecyclerView.ViewHolder(viewBinding.root) {
        var data: MessageInfo? = null
        init {
            viewBinding.root.setOnClickListener {
                data?.run {

                }
            }
        }
        fun bind(messageInfo: MessageInfo) {
            this.data = messageInfo
            viewBinding.tvUserName.text = messageInfo.fromUser
            viewBinding.tvTitle.text = messageInfo.title
            viewBinding.tvContent.text = messageInfo.message
            viewBinding.tvDate.text = messageInfo.niceDate
            viewBinding.tvTag.text = messageInfo.tag
        }
    }
}