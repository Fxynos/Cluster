package com.vl.cluster.api.definition.entity

import com.vl.cluster.api.definition.Session

data class PrivateDialog(
    override val session: Session,
    override val dialogId: Long,
    val companion: Profile,
    override val lastMessage: PrivateMessage?,
    override val imageUrl: String?
): Dialog