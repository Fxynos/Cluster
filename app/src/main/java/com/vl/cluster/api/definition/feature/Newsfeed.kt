package com.vl.cluster.api.definition.feature

import com.vl.cluster.api.definition.entity.Comment
import com.vl.cluster.api.definition.entity.Page
import com.vl.cluster.api.definition.entity.Post
import com.vl.cluster.api.definition.entity.Profile

interface Newsfeed {
    fun getPost(postId: Long): Post
    fun getComment(commentId: Long): Comment
    fun getProfile(profileId: Long): Profile
    fun fetchNews(source: Profile? = null, count: Int, key: String? = null): Page<String, Post>
    fun fetchComments(post: Post, count: Int, key: String? = null): Page<String, Comment>

    /**
     * If like is already set, unset it
     * @return the post after applying setting or unsetting like on it
     */
    fun setLike(post: Post): Post

    /**
     * @return full described comment after publishing
     */
    fun leaveComment(comment: Comment): Comment
}