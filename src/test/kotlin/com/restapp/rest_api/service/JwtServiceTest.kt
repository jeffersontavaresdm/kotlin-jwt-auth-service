package com.restapp.rest_api.service

import com.restapp.rest_api.utils.TestUtils
import io.jsonwebtoken.security.SignatureException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.util.ReflectionTestUtils
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class JwtServiceTest {
    private lateinit var jwtService: JwtService
    private val testUser = TestUtils.createTestUser()

    @BeforeEach
    fun setup() {
        jwtService = JwtService()
        ReflectionTestUtils.setField(jwtService, "secret", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970")
        ReflectionTestUtils.setField(jwtService, "expiration", "3600000")
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", "86400000")
    }

    @Test
    fun `should generate access token successfully`() {
        val token = jwtService.generateAccessToken(testUser.email)
        
        assertNotNull(token)
        assertTrue(jwtService.validateToken(token))
        assertEquals(testUser.email, jwtService.getEmailFromToken(token))
    }

    @Test
    fun `should generate refresh token successfully`() {
        val token = jwtService.generateRefreshToken(testUser.email)
        
        assertNotNull(token)
        assertTrue(jwtService.validateToken(token))
        assertEquals(testUser.email, jwtService.getEmailFromToken(token))
    }

    @Test
    fun `should validate token successfully`() {
        val token = jwtService.generateAccessToken(testUser.email)
        assertTrue(jwtService.validateToken(token))
    }

    @Test
    fun `should throw exception for invalid token`() {
        assertThrows<SignatureException> {
            jwtService.validateToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNTE2MjM5MDIyfQ.invalid_signature")
        }
    }

    @Test
    fun `should extract email from token successfully`() {
        val token = jwtService.generateAccessToken(testUser.email)
        val extractedEmail = jwtService.getEmailFromToken(token)
        
        assertEquals(testUser.email, extractedEmail)
    }
} 