package com.example.exam3.domain.repository

import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.model.Product


interface ProductsRepository {
    suspend fun getProducts(): AuthResult<List<Product>>


    suspend fun createProduct(
        name: String,
        description: String,
        year: Int
    ): AuthResult<Product>

    suspend fun updateProduct(
        id: String,
        name: String,
        description: String,
        year: Int
    ): AuthResult<Product>

    suspend fun deleteProduct(id: String): AuthResult<Unit>  // Добавляем удаление
}