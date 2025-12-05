package com.example.exam3.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.model.Product
import com.example.exam3.domain.usecase.CreateProductUseCase
import com.example.exam3.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val createProductUseCase: CreateProductUseCase
) : ViewModel() {

    private val _productName = mutableStateOf("")
    val productName: State<String> = _productName

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isSuccess = mutableStateOf(false)
    val isSuccess: State<Boolean> = _isSuccess

    fun updateName(name: String) {
        _productName.value = name
        if (_error.value != null) _error.value = null
        // Не сбрасываем isSuccess здесь, чтобы пользователь увидел сообщение
    }

    fun createProduct() {
        if (_productName.value.isBlank()) {
            _error.value = "Введите название"
            return
        }

        _error.value = null
        _isLoading.value = true
        _isSuccess.value = false

        viewModelScope.launch {
            when (val result = createProductUseCase(_productName.value)) {
                is AuthResult.Success -> {
                    _isSuccess.value = true
                }
                is AuthResult.Failure -> {
                    _error.value = result.exception.message ?: "Ошибка создания"
                }
            }
            _isLoading.value = false
        }
    }


}