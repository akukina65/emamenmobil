package com.example.exam3.data.remote

import com.example.exam3.data.dto.AuthResponseDto
import com.example.exam3.data.dto.LoginRequestDto
import dagger.Module
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
interface AuthApi {

@POST("auth/v1/token?grant_type=password")
suspend fun login(@Body loginRequest: LoginRequestDto):Response<AuthResponseDto>


}