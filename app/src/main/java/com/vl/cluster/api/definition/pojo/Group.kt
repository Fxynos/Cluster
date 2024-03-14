package com.vl.cluster.api.definition.pojo

data class Group(
    override val networkId: Int,
    override val id: Long,
    override val name: Profile.Name,
    override val imageUrl: String?
): Profile