package com.maureen.wandevelop.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.maureen.wandevelop.entity.Banner


/**
 * @author lianml
 * @date 2024/12/14
 */
class BannerAdapter(bannerList: List<Banner>) :
    com.youth.banner.adapter.BannerAdapter<Banner, BannerAdapter.ViewHolder>(bannerList) {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return ViewHolder(imageView)
    }

    override fun onBindView(holder: ViewHolder, data: Banner, position: Int, size: Int) {
        holder.bind(data)
    }

    class ViewHolder(private val bannerView: ImageView) : RecyclerView.ViewHolder(bannerView) {
        private var banner: Banner? = null

        init {
            bannerView.setOnClickListener {
                banner?.apply {  }
            }
        }
        fun bind(banner: Banner) {
            this.banner = banner
            bannerView.load(banner.imagePath)
            /*Glide.with(bannerView)
                .load(banner.imagePath)
                .placeholder(R.drawable.bg_tag)
                .into(bannerView)*/
        }
    }
}