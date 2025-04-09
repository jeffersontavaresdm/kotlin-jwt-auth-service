package com.restapp.rest_api.controller

import com.restapp.rest_api.dto.LoginRequest
import com.restapp.rest_api.dto.LoginResponse
import com.restapp.rest_api.dto.RefreshTokenRequest
import com.restapp.rest_api.dto.RegisterRequest
import com.restapp.rest_api.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(authService.register(request))
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authService.login(loginRequest)
        return if (response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.status(401).build()
        }
    }
    
    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshTokenRequest): ResponseEntity<LoginResponse> {
        val response = authService.refreshToken(request)
        return if (response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.status(401).build()
        }
    }
} 