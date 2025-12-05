package com.example.exam3.data.mapper

import com.example.exam3.data.dto.AuthResponseDto
import com.example.exam3.domain.model.AuthData
import com.example.exam3.domain.model.AuthUser

object AuthMapper {

    fun  toDomain(authResponse: AuthResponseDto): AuthData
    {
        return AuthData(
            accessToken = authResponse.access_token,
            refreshToken = authResponse.refresh_token,
            user = AuthUser(
                id = authResponse.user.id,
                email = authResponse.user.email
            )
        )
    }
}