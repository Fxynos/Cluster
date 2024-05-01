package com.vl.cluster.api.definition.entity

import com.vl.cluster.api.definition.entity.attachment.Attachment

sealed interface Message: SessionSpecificEntity {
    val messageId: Long
    val senderId: Long
    val text: String
    val attachments: List<Attachment>
}