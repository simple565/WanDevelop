package com.maureen.wandevelop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maureen.wandevelop.databinding.ItemArticleBinding
import com.maureen.wandevelop.entity.ArticleInfo
import com.maureen.wandevelop.ext.fromHtml

/**
 * Function: 文章列表适配器
 *
 * @author lianml
 * Create 2021-02-18
 */
class ArticleListAdapter(private val itemClick: (View, ArticleInfo) -> Unit) :
    ListAdapter<ArticleInfo, ArticleListAdapter.ViewHolder>(diffCallback) {

    companion object {
        private const val TAG = "HomePageArticleAdapter"
        private val diffCallback = object : DiffUtil.ItemCallback<ArticleInfo>() {
            override fun areItemsTheSame(oldItem: ArticleInfo, newItem: ArticleInfo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ArticleInfo, newItem: ArticleInfo): Boolean {
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
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val viewBinding: ItemArticleBinding,
        private val itemClick: (View, ArticleInfo) -> Unit
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        var data: ArticleInfo? = null

        init {
            viewBinding.root.setOnClickListener {
                data?.run { itemClick.invoke(it, this) }
            }
            viewBinding.ivMoreAction.setOnClickListener {
                data?.run { itemClick.invoke(it, this) }
            }
        }

        fun bind(item: ArticleInfo) {
            this.data = item
            viewBinding.tvTop.visibility = if (item.top) View.VISIBLE else View.GONE
            viewBinding.tvNewest.visibility = if (item.fresh) View.VISIBLE else View.GONE
            viewBinding.tvTitle.text = item.title.fromHtml()
            viewBinding.tvBrief.text = item.desc.fromHtml()
            viewBinding.tvAuthor.text = item.author.ifEmpty { item.shareUser }
            viewBinding.tvSuperCategory.text = item.superChapterName
            viewBinding.tvCategory.text = item.chapterName
        }
    }
}