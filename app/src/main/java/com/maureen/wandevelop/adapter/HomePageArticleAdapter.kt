package com.maureen.wandevelop.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maureen.wandevelop.databinding.ItemArticleBinding
import com.maureen.wandevelop.network.ArticleBean

/**
 * Function:
 *
 * @author lianml
 * Create 2021-02-18
 */
class HomePageArticleAdapter(private var list: MutableList<ArticleBean>?) : RecyclerView.Adapter<HomePageArticleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: $itemCount")
        holder.titleTv.text = list?.get(position)?.title
        holder.authorTv.text = list?.get(position)?.author
        holder.dateTv.text = list?.get(position)?.niceShareDate
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${list?.size}")
        return list?.size ?: 0
    }

    class ViewHolder(viewBinding: ItemArticleBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        val titleTv = viewBinding.itemArticleTvTitle
        val authorTv = viewBinding.itemArticleTvAuthor
        val dateTv = viewBinding.itemArticleTvDate
    }

    companion object {
        private const val TAG = "HomePageArticleAdapter"


    }
}