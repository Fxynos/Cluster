package com.vl.cluster.domain.entity

import com.vl.cluster.domain.boundary.Session

/**
 * @param source wall
 * @param owner author
 */
data class Post(
    override val session: Session,
    val postId: String,
    val source: Profile,
    val owner: Profile,
    val unixSec: Long,
    val text: String,
    val attachments: List<Attachment>,
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
): SessionSpecificEntity