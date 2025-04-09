package com.restapp.rest_api.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey
import io.jsonwebtoken.security.SignatureException

@Service
class JwtService {
    @Value("\${jwt.secret}")
    private lateinit var secret: String
    
    @Value("\${jwt.expiration}")
    private lateinit var expiration: String
    
    @Value("\${jwt.refresh-expiration}")
    private lateinit var refreshExpiration: String
    
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }
    
    fun generateAccessToken(email: String): String {
        return generateToken(email, expiration.toLong())
    }
    
    fun generateRefreshToken(email: String): String {
        return generateToken(email, refreshExpiration.toLong())
    }
    
    fun generateToken(email: String, expiration: Long): String {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
    
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            throw e
        } catch (e: Exception) {
            throw SignatureException("Invalid token signature")
        }
    }
    
    fun getEmailFromToken(token: String): String {
        try {
            return getClaims(token).subject
        } catch (e: io.jsonwebtoken.security.SignatureException) {
            throw e
        } catch (e: Exception) {
            throw io.jsonwebtoken.JwtException("Invalid token", e)
        }
    }
    
    private fun getClaims(token: String): Claims {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: io.jsonwebtoken.security.SignatureException) {
            throw e
        } catch (e: Exception) {
            throw io.jsonwebtoken.JwtException("Invalid token", e)
        }
    }
} 