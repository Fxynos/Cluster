package com.vl.cluster.domain.boundary

import com.vl.cluster.domain.entity.Comment
import com.vl.cluster.domain.entity.Page
import com.vl.cluster.domain.entity.Post
import com.vl.cluster.domain.entity.Profile

interface Newsfeed {
    fun getPost(postId: Long): Post
    fun getComment(commentId: Long): Comment
    fun getProfile(profileId: Long): Profile
    fun fetchNews(source: Profile? = null, count: Int, key: String? = null): Page<String, Post>
    fun fetchComments(post: Post, count: Int, key: String? = null): Page<String, Comment>

    fun setLike(postId: String, hasLike: Boolean)

    /**
     * @return full described comment after publishing
     */
    fun leaveComment(comment: Comment): Comment
}