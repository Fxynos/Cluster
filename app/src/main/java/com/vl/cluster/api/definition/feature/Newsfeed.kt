package com.vl.cluster.api.definition.feature

import com.vl.cluster.api.definition.pojo.Post
import com.vl.cluster.api.definition.pojo.Profile

interface Newsfeed {
    fun fetchNews(source: Profile? = null, count: Int, key: String? = null): List<Post>
}