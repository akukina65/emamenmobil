package com.example.exam3.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exam3.domain.model.AuthData
import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
):ViewModel(){

    private val _authState = mutableStateOf<AuthData?>(null)
    val authState:State<AuthData?> = _authState

    private  val _loading = mutableStateOf(false)
    val loading:State<Boolean> = _loading

    private val _error = mutableStateOf<String?>(null)
    val error:State<String?> = _error


    fun login(email:String, password:String)
    {
        if(email.isBlank()|| password.isBlank())
        {
            _error.value = "Введите email и пароль"
            return
        }
        _loading.value = true
        _error.value = null

        viewModelScope.launch {

            when (val result = loginUseCase(email,password))
            {
                is AuthResult.Success ->
                {
                    _authState.value = result.data
                    _error.value = null
                }

                is AuthResult.Failure ->
                {
                    _error.value = result.exception.message?:"Ошибка входа"
                    _authState.value = null
                }
            }
            _loading.value = false
        }

    }
}