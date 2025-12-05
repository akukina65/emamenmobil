package com.example.exam3.data.repository

import com.example.exam3.data.dto.LoginRequestDto
import com.example.exam3.data.mapper.AuthMapper
import com.example.exam3.domain.model.AuthData
import com.example.exam3.domain.model.AuthResult
import com.example.exam3.data.remote.AuthApi
import com.example.exam3.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoruIml @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {

    companion object {
        var currentToken: String? = null
    }

    override suspend fun login(email: String, password: String): AuthResult<AuthData> {
        return try {
            val response = authApi.login(LoginRequestDto(email, password))

                val authData = AuthMapper.toDomain(response.body()!!)
                // ✅ СОХРАНЯЕМ ТОКЕН
                currentToken = authData.accessToken
                AuthResult.Success(authData)

        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }
}