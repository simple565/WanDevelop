package com.maureen.wandevelop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maureen.wandevelop.databinding.ItemArticleBinding
import com.maureen.wandevelop.entity.Article
import com.maureen.wandevelop.ext.fromHtml

/**
 * 文章列表适配器
 * @author lianml
 * @date 2021-02-18
 */
class ArticleAdapter(private val itemClick: (View, Article) -> Unit) :
    ListAdapter<Article, ArticleAdapter.ViewHolder>(diffCallback) {

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
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val viewBinding: ItemArticleBinding,
        private val itemClick: (View, Article) -> Unit
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        var data: Article? = null

        init {
            viewBinding.root.setOnClickListener {
                data?.run { itemClick.invoke(it, this) }
            }
        }

        fun bind(item: Article) {
            this.data = item
            viewBinding.tvTop.visibility = if (item.top) View.VISIBLE else View.GONE
            viewBinding.tvNewest.visibility = if (item.fresh) View.VISIBLE else View.GONE
            viewBinding.tvTitle.text = item.title.fromHtml()
            viewBinding.tvAuthor.text = item.author.ifEmpty { item.shareUser }
            viewBinding.tvSuperCategory.text = item.superChapterName
            viewBinding.tvCategory.text = item.chapterName
        }
    }
}