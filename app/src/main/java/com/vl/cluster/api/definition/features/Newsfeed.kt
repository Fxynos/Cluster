package com.vl.cluster.api.definition.features

import com.vl.cluster.api.definition.pojo.Post

interface Newsfeed {
    fun nextPage(count: Int, key: String? = null): List<Post>
}