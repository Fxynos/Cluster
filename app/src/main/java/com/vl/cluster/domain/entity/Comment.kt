package com.vl.cluster.domain.entity

import com.vl.cluster.domain.boundary.Session

data class Comment(
    override val session: Session,
    val post: Post,
    val owner: Profile,
    val unixSec: Int,
    val text: String,
    val attachments: List<Attachment>,
    val likesCount: Long,
    val hasLike: Boolean,
    val canDelete: Boolean,
    val canEdit: Boolean
): SessionSpecificEntity