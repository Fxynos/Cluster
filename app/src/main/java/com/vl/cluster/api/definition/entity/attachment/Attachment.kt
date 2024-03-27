package com.vl.cluster.api.definition.entity.attachment

import com.vl.cluster.api.definition.entity.Profile
import com.vl.cluster.api.definition.entity.SessionSpecificEntity

interface Attachment: SessionSpecificEntity {
    val resourceUrl: String
    val owner: Profile
}