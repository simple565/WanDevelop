package com.maureen.wandevelop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.maureen.wandevelop.R
import com.maureen.wandevelop.databinding.ItemArticleBinding
import com.maureen.wandevelop.entity.Article
import com.maureen.wandevelop.ext.context
import com.maureen.wandevelop.ext.fromHtml

/**
 * 文章列表适配器
 * @author lianml
 * @date 2021-02-18
 */
class ArticleAdapter(private val itemClick: (View, Article) -> Unit) : PagingDataAdapter<Article, ArticleAdapter.ViewHolder>(diffCallback) {

    companion object {
        private const val TAG = "ArticleAdapter"
        private val diffCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.also { holder.bind(it) }
    }

    class ViewHolder(
        private val viewBinding: ItemArticleBinding,
        private val itemClick: (View, Article) -> Unit
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        var data: Article? = null

        init {
            viewBinding.root.setOnClickListener {
                data?.apply { itemClick.invoke(it, this) }
            }
            viewBinding.ivMoreAction.setOnClickListener {
                data?.apply { itemClick.invoke(it, this) }
            }
        }

        fun bind(item: Article) {
            this.data = item
            viewBinding.cgTag.removeAllViews()
            item.tags.forEach {
                val chip = LayoutInflater.from(viewBinding.cgTag.context).inflate(R.layout.item_tag, viewBinding.cgTag, false) as TextView
                chip.text = it.name
                chip.setTextColor(ContextCompat.getColor(viewBinding.cgTag.context, it.getTextColorResId(viewBinding.cgTag.context)))
                chip.setBackgroundResource(it.getBackgroundDrawableResId(viewBinding.cgTag.context))
                viewBinding.cgTag.addView(chip)
            }
            viewBinding.tvDate.text = item.niceDate.ifBlank { item.niceShareDate }
            viewBinding.tvTitle.text = item.title.fromHtml()
            viewBinding.tvAuthor.text = item.author.ifBlank { item.shareUser }
        }
    }
}