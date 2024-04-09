package com.vl.cluster.api.definition.entity

import com.vl.cluster.api.definition.Session

interface SessionSpecificEntity {
    val session: Session
}