package com.example.exam3.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.usecase.CreateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val createProductUseCase: CreateProductUseCase
) : ViewModel() {

    private val _productName = mutableStateOf("")
    val productName: State<String> = _productName

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _year = mutableStateOf("")
    val year: State<String> = _year

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isSuccess = mutableStateOf(false)
    val isSuccess: State<Boolean> = _isSuccess

    fun updateName(name: String) {
        _productName.value = name
        if (_error.value != null) _error.value = null
    }

    fun updateDescription(description: String) {
        _description.value = description
        if (_error.value != null) _error.value = null
    }

    fun updateYear(year: String) {
        _year.value = year
        if (_error.value != null) _error.value = null
    }

    fun createProduct() {
        if (_productName.value.isBlank()) {
            _error.value = "Введите название"
            return
        }

        if (_description.value.isBlank()) {
            _error.value = "Введите описание"
            return
        }

        if (_year.value.isBlank()) {
            _error.value = "Введите год"
            return
        }

        val yearInt = try {
            _year.value.toInt()
        } catch (e: NumberFormatException) {
            _error.value = "Введите корректный год"
            return
        }

        _error.value = null
        _isLoading.value = true
        _isSuccess.value = false

        viewModelScope.launch {
            when (val result = createProductUseCase(
                name = _productName.value,
                description = _description.value,
                year = yearInt
            )) {
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