package com.maureen.wandevelop.ext

import android.content.Context
import com.maureen.wandevelop.R
import com.maureen.wandevelop.entity.Article
import com.maureen.wandevelop.entity.Tag

/**
 * @author lianml
 * @date 2025/2/4
 */
/**
 * 整理标签
 */
fun Article.fixTags(context: Context): Article {
    val newTags = mutableListOf<Tag>()
    if (this.fresh) {
        newTags.add(Tag(context.getString(R.string.newest), ""))
    }
    if (this.top) {
        newTags.add(Tag(context.getString(R.string.newest), ""))
    }
    newTags.addAll(this.tags)
    if (this.superChapterName.isNotBlank() && newTags.none { it.name == this.superChapterName }) {
        newTags.add(Tag(this.superChapterName, ""))
    }
    if (this.chapterName.isNotBlank() && newTags.none { it.name == this.chapterName }) {
        newTags.add(Tag(this.chapterName, ""))
    }
    return this.copy(tags = newTags)
}

val Article.displayAuthor: String
    get() = listOf(this.author, this.shareUser).firstOrNull { !it.isNullOrBlank() } ?: ""


val Article.displayPublishData: String
    get() = listOf(this.niceDate, this.niceShareDate).firstOrNull { !it.isNullOrBlank() }
        ?.substringBefore(" ") ?: ""