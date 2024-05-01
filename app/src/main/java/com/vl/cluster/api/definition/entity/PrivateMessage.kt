package com.vl.cluster.api.definition.entity

import com.vl.cluster.api.definition.Session
import com.vl.cluster.api.definition.entity.attachment.Attachment

/**
 * @param companionId receiver's profile id
 */
data class PrivateMessage(
    override val session: Session,
    override val messageId: Long,
    override val senderId: Long,
    val companionId: Long,
    override val text: String,
    override val attachments: List<Attachment>
): Message