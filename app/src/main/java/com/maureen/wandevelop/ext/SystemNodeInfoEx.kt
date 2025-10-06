package com.maureen.wandevelop.ext

import com.maureen.wandevelop.core.entity.Feed
import com.maureen.wandevelop.network.entity.SystemNodeInfo

/**
 * @author lianml
 * @date 2025/2/9
 */
fun SystemNodeInfo.toFeed(): Feed {
    return Feed(
        id = this.id,
        title = this.name,
        coverUrl = this.cover,
        author = this.author,
        description = this.desc.trim()
    )
}