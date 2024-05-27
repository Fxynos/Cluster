package com.vl.cluster.domain.entity

import com.vl.cluster.domain.boundary.Session


sealed interface Attachment: SessionSpecificEntity {
    val resourceUrl: String
    val owner: Profile

    data class Image(
        override val session: Session,
        override val resourceUrl: String,
        override val owner: Profile
    ): Attachment
}