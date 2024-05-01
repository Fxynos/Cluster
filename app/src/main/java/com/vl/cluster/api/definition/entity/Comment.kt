package com.vl.cluster.api.definition.entity

import com.vl.cluster.api.definition.Session
import com.vl.cluster.api.definition.entity.attachment.Attachment

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