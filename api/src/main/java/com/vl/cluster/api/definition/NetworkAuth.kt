package com.vl.cluster.api.definition

interface NetworkAuth {
    enum class LoginType {
        PHONE,
        EMAIL,
        ID
    }

    enum class AuthType {
        CALL,
        SMS,
        PASSWORD
    }

    val loginVariants: Set<LoginType>
    val authType: AuthType

    /**
     * @return null if password is incorrect, token otherwise
     */
    fun signIn(login: String, password: String): String?
}