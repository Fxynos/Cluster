package com.vl.cluster.domain.entity

import com.vl.cluster.domain.boundary.Session

interface SessionSpecificEntity {
    val session: Session
}