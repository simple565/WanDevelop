package com.maureen.wandevelop.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maureen.wandevelop.R
import com.maureen.wandevelop.databinding.ItemArticleBinding
import com.maureen.wandevelop.ext.fromHtml
import com.maureen.wandevelop.network.ArticleBean

/**
 * Function:
 *
 * @author lianml
 * Create 2021-02-18
 */
class HomePageArticleAdapter(private var list: MutableList<ArticleBean>?) :
    RecyclerView.Adapter<HomePageArticleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list?.get(position)
        holder.topMark.visibility = if (data?.top == true) View.VISIBLE else View.GONE
        holder.freshTv.visibility = if (data?.fresh == true) View.VISIBLE else View.GONE
        holder.titleTv.text = data?.title?.fromHtml()
        var author = ""
        if (!TextUtils.isEmpty(data?.author)) {
            author = String.format(
                holder.authorTv.context.getString(R.string.which_author),
                data?.author
            )
        } else if (!TextUtils.isEmpty(data?.shareUser)) {
            author = String.format(
                holder.authorTv.context.getString(R.string.which_share_user),
                data?.shareUser
            )
        }
        if (!TextUtils.isEmpty(data?.desc)) {
            holder.desTv.text = data?.desc?.fromHtml()
        }
        holder.authorTv.text = author
        holder.dateTv.text = data?.niceShareDate
        holder.superCategoryTv.text = data?.superChapterName
        holder.categoryTv.text = data?.chapterName
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    class ViewHolder(viewBinding: ItemArticleBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        val topMark = viewBinding.itemArticleTvTop
        val freshTv = viewBinding.itemArticleTvFresh
        val titleTv = viewBinding.itemArticleTvTitle
        val authorTv = viewBinding.itemArticleTvAuthor
        val dateTv = viewBinding.itemArticleTvDate
        val desTv = viewBinding.itemArticleTvDescription
        val superCategoryTv = viewBinding.itemArticleTvSuperCategory
        val categoryTv = viewBinding.itemArticleTvCategory
    }

    companion object {
        private const val TAG = "HomePageArticleAdapter"
    }
}