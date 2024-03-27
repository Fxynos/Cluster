package com.vl.cluster.api.definition.feature

import com.vl.cluster.api.definition.entity.Comment
import com.vl.cluster.api.definition.entity.Page
import com.vl.cluster.api.definition.entity.Post
import com.vl.cluster.api.definition.entity.Profile

interface Newsfeed {
    fun fetchNews(source: Profile? = null, count: Int, key: String? = null): Page<String, Post>
    fun fetchComments(post: Post, count: Int, key: String? = null): Page<String, Comment>
}