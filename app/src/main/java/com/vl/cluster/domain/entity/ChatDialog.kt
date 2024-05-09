package com.vl.cluster.domain.entity

import com.vl.cluster.domain.boundary.Session

data class ChatDialog(
    override val session: Session,
    override val dialogId: Long,
    val companions: List<Profile>,
    override val lastMessage: ChatMessage?,
    override val imageUrl: String?
): Dialog