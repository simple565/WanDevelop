package com.maureen.wandevelop.network

/**
 * Function:
 * @author lianml
 * Create 2021-02-15
 */
open class BaseResponse<T> {
    val data: T? = null
    val errorMsg: String? = null
    val errorCode: Int = 0
}

data class Tag(val name: String, val url: String)

data class ArticleBean(
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
        val id: Int,
        val link: String,
        val niceDate: String,
        val niceShareDate: String,
        val origin: String,
        val prefix: String,
        val projectLink: String,
        val publishTime: Long,
        val realSuperChapter: Int,
        val selfVisible: Int,
        val shareDate: String,
        val shareUser: String,
        val superChapterId: Int,
        val superChapterName: String,
        val tags: MutableList<Tag>,
        val title: String,
        val type: Int,
        val userId: Int,
        val visible: Int,
        val zan: Int,
)

data class ArticleListBean(
        val curPage: Int = 0,
        val datas: MutableList<ArticleBean>,
        val offset: Int = 0,
        val over: Boolean = false,
        val pageCount: Int = 0,
        val size: Int = 0,
        val total: Int = 0
)