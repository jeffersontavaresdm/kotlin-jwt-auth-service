package com.restapp.rest_api.utils

import com.restapp.rest_api.model.User
import com.restapp.rest_api.dto.LoginRequest
import com.restapp.rest_api.dto.RegisterRequest
import com.restapp.rest_api.dto.RefreshTokenRequest

object TestUtils {
    fun createTestUser(
        id: Long = 1L,
        email: String = "test@example.com",
        password: String = "hashedPassword123",
        name: String = "Test User"
    ) = User(
        id = id,
        email = email,
        password = password,
        name = name
    )

    fun createTestLoginRequest(
        email: String = "test@example.com",
        password: String = "password123"
    ) = LoginRequest(
        email = email,
        password = password
    )

    fun createTestRegisterRequest(
        email: String = "test@example.com",
        password: String = "password123",
        name: String = "Test User"
    ) = RegisterRequest(
        email = email,
        password = password,
        name = name
    )

    fun createTestRefreshTokenRequest(
        refreshToken: String = "test.refresh.token"
    ) = RefreshTokenRequest(
        refreshToken = refreshToken
    )

    const val TEST_JWT_TOKEN = "test.jwt.token"
    const val TEST_REFRESH_TOKEN = "test.refresh.token"
} 