package com.maureen.wandevelop.ext

import android.content.Context
import com.maureen.wandevelop.R
import com.maureen.wandevelop.common.entity.Feed
import com.maureen.wandevelop.entity.ArticleInfo
import com.maureen.wandevelop.entity.TagInfo

fun ArticleInfo.getDisplayTags(context: Context): List<TagInfo> {
    val newTags = mutableListOf<TagInfo>()
    if (this.fresh) {
        newTags.add(TagInfo(context.getString(R.string.newest), ""))
    }
    if (this.top) {
        newTags.add(TagInfo(context.getString(R.string.newest), ""))
    }
    newTags.addAll(this.tags)
    if (this.superChapterName.isNotBlank() && newTags.none { it.name == this.superChapterName }) {
        newTags.add(TagInfo(this.superChapterName, ""))
    }
    if (this.chapterName.isNotBlank() && newTags.none { it.name == this.chapterName }) {
        newTags.add(TagInfo(this.chapterName, ""))
    }
    return newTags
}

val ArticleInfo.displayAuthor: String
    get() = listOf(this.author, this.shareUser).firstOrNull { !it.isNullOrBlank() } ?: ""

val ArticleInfo.displayPublishData: String
    get() = listOf(this.niceDate, this.niceShareDate).firstOrNull { !it.isNullOrBlank() }
        ?.substringBefore(" ") ?: ""

fun ArticleInfo.toFeed(context: Context): Feed {
    return Feed(
        id = this.id,
        title = this.title,
        url = this.link,
        isCollect = this.collect,
        author = this.displayAuthor,
        publishData = this.displayPublishData,
        tags = this.getDisplayTags(context)
    )
}