package com.example.exam3.domain.model

data class AuthData (
    val accessToken:String,
    val refreshToken:String,
    val user: AuthUser
)
