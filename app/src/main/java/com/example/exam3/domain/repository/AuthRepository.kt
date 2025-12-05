package com.example.exam3.domain.repository

import com.example.exam3.domain.model.AuthData
import com.example.exam3.domain.model.AuthResult

interface AuthRepository {

    suspend fun login(email:String, password:String): AuthResult<AuthData>
}