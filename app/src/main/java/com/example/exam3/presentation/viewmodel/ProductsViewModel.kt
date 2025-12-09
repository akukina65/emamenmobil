package com.example.exam3.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.model.Product
import com.example.exam3.domain.usecase.DeleteProductUseCase
import com.example.exam3.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    private val _products = mutableStateOf<List<Product>>(emptyList())
    val products = _products

    private val _filteredProducts = mutableStateOf<List<Product>>(emptyList())
    val filteredProducts = _filteredProducts

    private val _loading = mutableStateOf(false)
    val loading = _loading

    private val _error = mutableStateOf<String?>(null)
    val error = _error

    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

    fun loadProducts() {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            when (val result = getProductsUseCase()) {
                is AuthResult.Success -> {
                    _products.value = result.data
                    applyFilter(_searchQuery.value)
                }
                is AuthResult.Failure -> {
                    _error.value = result.exception.message ?: "Ошибка загрузки"
                }
            }
            _loading.value = false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applyFilter(query)
    }



    private fun applyFilter(query: String) {
        _filteredProducts.value = if (query.isBlank()) {
            _products.value
        } else {
            _products.value.filter { product ->
                product.name.contains(query, ignoreCase = true)

            }
        }
    }

    // Метод для удаления продукта
    fun deleteProduct(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = deleteProductUseCase(id)) {
                is AuthResult.Success -> {
                    // Удаляем продукт из списка
                    _products.value = _products.value.filter { it.id != id }
                    applyFilter(_searchQuery.value)
                    onSuccess()
                }
                is AuthResult.Failure -> {
                    _error.value = result.exception.message ?: "Ошибка удаления"
                }
            }
            _loading.value = false
        }
    }

    // Поиск продукта по ID для отображения в диалоге
    fun findProductById(id: String): Product? {
        return _products.value.find { it.id == id }
    }
}