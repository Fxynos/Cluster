package com.vl.cluster.domain.entity

sealed interface Message: SessionSpecificEntity {
    val messageId: Long
    val senderId: Long
    val text: String
    val attachments: List<Attachment>
}