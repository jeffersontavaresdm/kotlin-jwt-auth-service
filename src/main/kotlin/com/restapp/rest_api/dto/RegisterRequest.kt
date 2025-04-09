package com.restapp.rest_api.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
) 