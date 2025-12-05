package com.example.exam3.data.dto

data class AuthResponseDto(

    val access_token:String,
    val token_type:String,
    val expires_in:Int,
    val refresh_token:String,
    val user: UserDto
)
