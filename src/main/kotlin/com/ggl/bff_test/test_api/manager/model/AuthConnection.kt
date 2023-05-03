package com.ggl.bff_test.test_api.manager.model

data class AuthConnection(
    val authServerUrl: String?,
    val grantType: String?,
    val clientId: String?,
    val clientSecret: String?,
    val username: String?,
    val password: String?
)
