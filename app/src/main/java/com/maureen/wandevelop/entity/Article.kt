package com.maureen.wandevelop.entity

import android.content.Context
import com.maureen.wandevelop.R
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val adminAdd: Boolean,
    val apkLink: String,
    val audit: Int,
    val author: String,
    val canEdit: Boolean,
    val chapterId: Int,
    val chapterName: String,
    var collect: Boolean,
    val courseId: Int,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val host: String,
    val id: Long,
    val isAdminAdd: Boolean,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: List<Tag>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int,
    val top: Boolean = false
): java.io.Serializable {
    fun refreshTags(context: Context): Article {
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
}