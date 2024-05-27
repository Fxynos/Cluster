package com.vl.cluster.domain.entity

sealed interface Profile: NetworkSpecificEntity {
    val profileId: String
    val name: String
    val imageUrl: String?
}