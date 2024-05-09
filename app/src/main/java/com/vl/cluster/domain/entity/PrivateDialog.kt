package com.vl.cluster.domain.entity

import com.vl.cluster.domain.boundary.Session

data class PrivateDialog(
    override val session: Session,
    override val dialogId: Long,
    val companion: Profile,
    override val lastMessage: PrivateMessage?,
    override val imageUrl: String?
): Dialog