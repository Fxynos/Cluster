package com.vl.cluster.api.definition.pojo

sealed interface Profile: NetworkSpecificEntity {
    val id: Long
    val name: String
    val imageUrl: String?
}