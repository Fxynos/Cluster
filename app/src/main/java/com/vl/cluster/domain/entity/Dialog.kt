package com.vl.cluster.domain.entity

sealed interface Dialog: SessionSpecificEntity {
    val dialogId: Long
    val lastMessage: Message?
    val imageUrl: String?
}