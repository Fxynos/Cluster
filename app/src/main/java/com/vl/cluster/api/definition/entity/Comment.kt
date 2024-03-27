package com.vl.cluster.api.definition.entity

import com.vl.cluster.api.definition.Network
import com.vl.cluster.api.definition.Session

class Comment( // TODO [tva] attachments and reposts
    override val session: Session,
    val post: Post,
    val owner: Profile,
    val unixSec: Int,
    val text: String,
    val likesCount: Long,
    val hasLike: Boolean,
    val canDelete: Boolean,
    val canEdit: Boolean
): SessionSpecificEntity