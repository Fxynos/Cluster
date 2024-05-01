package com.vl.cluster.api.definition.entity

sealed interface Dialog: SessionSpecificEntity {
    val dialogId: Long
    val lastMessage: Message?
    val imageUrl: String?
}