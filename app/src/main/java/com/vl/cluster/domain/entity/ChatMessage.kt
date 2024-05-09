package com.vl.cluster.domain.entity

import com.vl.cluster.domain.boundary.Session

data class ChatMessage(
    override val session: Session,
    override val messageId: Long,
    override val senderId: Long,
    val chatId: Long,
    override val text: String,
    override val attachments: List<Attachment>
): Message