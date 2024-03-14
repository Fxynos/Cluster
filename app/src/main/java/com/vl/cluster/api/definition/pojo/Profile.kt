package com.vl.cluster.api.definition.pojo

sealed interface Profile: NetworkEntity {
    val id: Long
    val name: Name
    val imageUrl: String?

    sealed interface Name {
        val name: String
    }

    data class FirstAndLastName(val firstname: String, val lastname: String): Name {
        override val name: String
            get() = "$firstname $lastname"
    }

    data class Username(override val name: String): Name
}