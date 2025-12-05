package com.example.exam3.domain.repository

import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.model.Product


interface ProductsRepository {
    suspend fun getProducts(): AuthResult<List<Product>>


    suspend fun createProduct(name: String): AuthResult<Product>  // Добавили
}