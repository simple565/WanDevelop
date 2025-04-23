package com.maureen.wandevelop.ext

import com.maureen.wandevelop.common.entity.Feed
import com.maureen.wandevelop.entity.CourseInfo

/**
 * @author lianml
 * @date 2025/2/9
 */
fun CourseInfo.toFeed(): Feed {
    return Feed(
        id = this.id,
        title = this.name,
        coverUrl = this.cover,
        author = this.author,
        description = this.desc.trim()
    )
}