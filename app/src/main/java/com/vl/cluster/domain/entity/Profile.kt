package com.vl.cluster.domain.entity

sealed interface Profile: NetworkSpecificEntity {
    val profileId: Long
    val name: String
    val imageUrl: String?
}