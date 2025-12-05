package com.example.exam3.domain.usecase

import com.example.exam3.domain.model.AuthData
import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email:String, password:String): AuthResult<AuthData>
    {
        return authRepository.login(email,password)
    }
}