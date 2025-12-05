package com.example.exam3.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.model.Product
import com.example.exam3.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    // Все продукты (оригинальный список)
    private var _allProducts by mutableStateOf<List<Product>>(emptyList())

    // Отфильтрованные продукты (для отображения) - ДЕЛАЕМ ПРОСТОЙ MutableState
    val filteredProducts = mutableStateOf<List<Product>>(emptyList())

    // Текст поиска - ДЕЛАЕМ ПРОСТОЙ MutableState
    val searchQuery = mutableStateOf("")

    // Состояния - ДЕЛАЕМ ПРОСТЫЕ MutableState
    val loading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    // Загрузка продуктов
    fun loadProducts() {
        if (loading.value) return

        loading.value = true
        error.value = null

        viewModelScope.launch {
            when (val result = getProductsUseCase()) {
                is AuthResult.Success -> {
                    _allProducts = result.data
                    applySearchFilter() // Применяем текущий фильтр
                    error.value = null
                }
                is AuthResult.Failure -> {
                    error.value = result.exception.message ?: "Неизвестная ошибка"
                    _allProducts = emptyList()
                    filteredProducts.value = emptyList()
                }
            }
            loading.value = false
        }
    }

    // Обновление поискового запроса
    fun updateSearchQuery(query: String) {
        searchQuery.value = query
        applySearchFilter()
    }

    // Очистка поиска
    fun clearSearch() {
        searchQuery.value = ""
        filteredProducts.value = _allProducts
    }

    // Применение фильтра поиска
    private fun applySearchFilter() {
        if (searchQuery.value.isBlank()) {
            filteredProducts.value = _allProducts
        } else {
            val query = searchQuery.value.lowercase()
            filteredProducts.value = _allProducts.filter { product ->
                product.name.lowercase().contains(query)
            }
        }
    }


}