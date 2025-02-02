package com.maureen.wandevelop.feature.profile.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.maureen.wandevelop.databinding.ItemBookmarkBinding
import com.maureen.wandevelop.entity.Collection
import com.maureen.wandevelop.ext.fromHtml

/**
 * 浏览记录、书签、稍后阅读列表适配器
 * @author lianml
 * @date 2024/2/17
 */
class BookmarkAdapter(private val itemLongClick: (Collection) -> Unit) :
    PagingDataAdapter<Collection, BookmarkAdapter.ViewHolder>(diffCallback) {

    companion object {
        private const val TAG = "BookmarkAdapter"
        private val diffCallback = object : DiffUtil.ItemCallback<Collection>() {
            override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent, itemLongClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val viewBinding: ItemBookmarkBinding,
        private val itemClick: (Collection) -> Unit
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        companion object {
            fun create(parent: ViewGroup, itemClick: (Collection) -> Unit): ViewHolder {
                return ViewHolder(
                    ItemBookmarkBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), itemClick
                )
            }
        }

        var data: Collection? = null

        init {
            viewBinding.root.setOnClickListener {
                // TODO: web页面
            }
            viewBinding.root.setOnLongClickListener {
                data?.run { itemClick.invoke(this) }
                return@setOnLongClickListener true
            }
        }

        fun bind(item: Collection?) {
            this.data = item
            if (item == null) {
                return
            }
            viewBinding.tvTitle.text = item.title.fromHtml()
            viewBinding.tvAuthor.text = item.author.ifEmpty { "匿名用户" }
            viewBinding.tvBookmarkDate.text = String.format("收藏时间：%s", item.niceDate)
            viewBinding.tvCategory.text = item.chapterName
            viewBinding.tvCategory.isVisible = item.chapterName.isNotEmpty()
        }
    }
}