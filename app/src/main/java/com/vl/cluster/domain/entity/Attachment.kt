package com.vl.cluster.domain.entity

import com.vl.cluster.domain.entity.Profile
import com.vl.cluster.domain.entity.SessionSpecificEntity

interface Attachment: SessionSpecificEntity {
    val resourceUrl: String
    val owner: Profile
}