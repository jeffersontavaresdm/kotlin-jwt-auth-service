package com.restapp.rest_api.service

import com.restapp.rest_api.dto.LoginRequest
import com.restapp.rest_api.dto.LoginResponse
import com.restapp.rest_api.dto.RefreshTokenRequest
import com.restapp.rest_api.dto.RegisterRequest
import com.restapp.rest_api.model.User
import com.restapp.rest_api.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtService: JwtService
) {
    fun register(request: RegisterRequest): LoginResponse {
        val hashedPassword = passwordEncoder.encode(request.password)
        val user = User(
            email = request.email,
            password = hashedPassword,
            name = request.name
        )
        userRepository.save(user)
        
        val accessToken = jwtService.generateAccessToken(user.email)
        val refreshToken = jwtService.generateRefreshToken(user.email)
        
        return LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            email = user.email,
            name = user.name
        )
    }

    fun login(loginRequest: LoginRequest): LoginResponse? {
        val user = userRepository.findByEmail(loginRequest.email) ?: return null
        
        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            return null
        }
        
        val accessToken = jwtService.generateAccessToken(user.email)
        val refreshToken = jwtService.generateRefreshToken(user.email)
        
        return LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            email = user.email,
            name = user.name
        )
    }
    
    fun refreshToken(request: RefreshTokenRequest): LoginResponse? {
        if (!jwtService.validateToken(request.refreshToken)) {
            return null
        }
        
        val email = jwtService.getEmailFromToken(request.refreshToken)
        val user = userRepository.findByEmail(email) ?: return null
        
        val accessToken = jwtService.generateAccessToken(email)
        
        return LoginResponse(
            accessToken = accessToken,
            refreshToken = request.refreshToken,
            email = user.email,
            name = user.name
        )
    }
} 