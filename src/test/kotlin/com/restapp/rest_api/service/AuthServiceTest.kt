package com.restapp.rest_api.service

import com.restapp.rest_api.dto.LoginRequest
import com.restapp.rest_api.dto.RefreshTokenRequest
import com.restapp.rest_api.dto.LoginResponse
import com.restapp.rest_api.model.User
import com.restapp.rest_api.repository.UserRepository
import com.restapp.rest_api.utils.TestUtils
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.junit.jupiter.api.extension.ExtendWith
import io.mockk.junit5.MockKExtension
import io.jsonwebtoken.JwtException

@ExtendWith(MockKExtension::class)
class AuthServiceTest {
    private val userRepository: UserRepository = mockk()
    private val passwordEncoder: BCryptPasswordEncoder = mockk()
    private val jwtService: JwtService = mockk()
    private lateinit var authService: AuthService

    @BeforeEach
    fun setup() {
        authService = AuthService(userRepository, passwordEncoder, jwtService)
    }

    @Test
    fun `register should create new user and return tokens`() {
        val registerRequest = TestUtils.createTestRegisterRequest()
        val hashedPassword = "hashedPassword123"
        val user = TestUtils.createTestUser()
        val accessToken = TestUtils.TEST_JWT_TOKEN
        val refreshToken = TestUtils.TEST_REFRESH_TOKEN

        every { userRepository.findByEmail(registerRequest.email) } returns null
        every { passwordEncoder.encode(registerRequest.password) } returns hashedPassword
        every { userRepository.save(any()) } returns user
        every { jwtService.generateAccessToken(user.email) } returns accessToken
        every { jwtService.generateRefreshToken(user.email) } returns refreshToken

        val result = authService.register(registerRequest)

        assert(result.accessToken == accessToken)
        assert(result.refreshToken == refreshToken)
        verify { userRepository.save(any()) }
    }

    @Test
    fun `register should throw exception when email already exists`() {
        val registerRequest = TestUtils.createTestRegisterRequest()
        val existingUser = TestUtils.createTestUser()

        every { userRepository.findByEmail(registerRequest.email) } returns existingUser

        assertThrows<IllegalArgumentException> {
            authService.register(registerRequest)
        }

        verify(exactly = 0) { passwordEncoder.encode(any()) }
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `should login successfully with valid credentials`() {
        val loginRequest = TestUtils.createTestLoginRequest()
        val user = TestUtils.createTestUser()
        val accessToken = TestUtils.TEST_JWT_TOKEN
        val refreshToken = TestUtils.TEST_REFRESH_TOKEN

        every { userRepository.findByEmail(loginRequest.email) } returns user
        every { passwordEncoder.matches(loginRequest.password, user.password) } returns true
        every { jwtService.generateAccessToken(loginRequest.email) } returns accessToken
        every { jwtService.generateRefreshToken(loginRequest.email) } returns refreshToken

        val result = authService.login(loginRequest)

        assert(result != null)
        assert(result?.accessToken == accessToken)
        assert(result?.refreshToken == refreshToken)
        verify { userRepository.findByEmail(loginRequest.email) }
        verify { passwordEncoder.matches(loginRequest.password, user.password) }
        verify { jwtService.generateAccessToken(loginRequest.email) }
        verify { jwtService.generateRefreshToken(loginRequest.email) }
    }

    @Test
    fun `should return null when login fails`() {
        val loginRequest = TestUtils.createTestLoginRequest()
        val user = TestUtils.createTestUser()

        every { userRepository.findByEmail(loginRequest.email) } returns user
        every { passwordEncoder.matches(loginRequest.password, user.password) } returns false

        val result = authService.login(loginRequest)

        assert(result == null)
        verify { userRepository.findByEmail(loginRequest.email) }
        verify { passwordEncoder.matches(loginRequest.password, user.password) }
        verify(exactly = 0) { jwtService.generateAccessToken(any()) }
        verify(exactly = 0) { jwtService.generateRefreshToken(any()) }
    }

    @Test
    fun `should refresh token successfully with valid refresh token`() {
        val refreshRequest = TestUtils.createTestRefreshTokenRequest()
        val user = TestUtils.createTestUser()
        val newAccessToken = "new.${TestUtils.TEST_JWT_TOKEN}"

        every { jwtService.validateToken(refreshRequest.refreshToken) } returns true
        every { jwtService.getEmailFromToken(refreshRequest.refreshToken) } returns user.email
        every { userRepository.findByEmail(user.email) } returns user
        every { jwtService.generateAccessToken(user.email) } returns newAccessToken

        val result = authService.refreshToken(refreshRequest)

        assert(result != null)
        assert(result?.accessToken == newAccessToken)
        assert(result?.refreshToken == refreshRequest.refreshToken)
        verify { jwtService.validateToken(refreshRequest.refreshToken) }
        verify { jwtService.getEmailFromToken(refreshRequest.refreshToken) }
        verify { userRepository.findByEmail(user.email) }
        verify { jwtService.generateAccessToken(user.email) }
    }

    @Test
    fun `should return null when refresh token is invalid`() {
        val refreshRequest = TestUtils.createTestRefreshTokenRequest()

        every { jwtService.validateToken(refreshRequest.refreshToken) } returns false

        val result = authService.refreshToken(refreshRequest)
        assert(result == null)

        verify { jwtService.validateToken(refreshRequest.refreshToken) }
        verify(exactly = 0) { jwtService.getEmailFromToken(any()) }
        verify(exactly = 0) { userRepository.findByEmail(any()) }
        verify(exactly = 0) { jwtService.generateAccessToken(any()) }
    }
}
