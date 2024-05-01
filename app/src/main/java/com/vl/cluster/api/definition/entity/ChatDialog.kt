package com.vl.cluster.api.definition.entity

import com.vl.cluster.api.definition.Session

data class ChatDialog(
    override val session: Session,
    override val dialogId: Long,
    val companions: List<Profile>,
    override val lastMessage: ChatMessage?,
    override val imageUrl: String?
): Dialog