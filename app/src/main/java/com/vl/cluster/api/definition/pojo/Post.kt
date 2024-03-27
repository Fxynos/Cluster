package com.vl.cluster.api.definition.pojo

import com.vl.cluster.api.definition.Network

data class Post( // TODO attachments and reposts
    override val network: Network,
    val postId: Long,
    val source: Profile,
    val owner: Profile,
    val unixSec: Long,
    val text: String,
    val viewsCount: Long,
    val likesCount: Long,
    val commentsCount: Long,
    val repostsCount: Long,
    val canLike: Boolean,
    val canComment: Boolean,
    val canRepost: Boolean,
    val canDelete: Boolean,
    val canEdit: Boolean,
    val hasLike: Boolean
): NetworkSpecificEntity