package com.vl.cluster.api.definition.entity

import com.vl.cluster.api.definition.Session
import com.vl.cluster.api.definition.entity.attachment.Attachment

data class Post(
    override val session: Session,
    val postId: Long,
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