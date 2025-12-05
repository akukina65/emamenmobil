// EditProductViewModel.kt
package com.example.exam3.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.usecase.UpdateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val updateProductUseCase: UpdateProductUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId = savedStateHandle.get<String>("id") ?: ""
    private val initialName = savedStateHandle.get<String>("name") ?: ""

    private val _productName = mutableStateOf(initialName)
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
    }

    fun updateProduct() {
        if (_productName.value.isBlank()) {
            _error.value = "Введите название"
            return
        }

        _error.value = null
        _isLoading.value = true
        _isSuccess.value = false

        viewModelScope.launch {
            when (val result = updateProductUseCase(productId, _productName.value)) {
                is AuthResult.Success -> {
                    _isSuccess.value = true
                }
                is AuthResult.Failure -> {
                    _error.value = result.exception.message ?: "Ошибка обновления"
                }
            }
            _isLoading.value = false
        }
    }
}