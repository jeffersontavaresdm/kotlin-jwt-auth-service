package com.restapp.rest_api.dto

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val email: String,
    val name: String
) 