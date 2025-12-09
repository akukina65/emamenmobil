package com.example.exam3.domain.usecase

import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.model.Product
import com.example.exam3.domain.repository.ProductsRepository
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(
        name: String,
        description: String,
        year: Int
    ): AuthResult<Product> {
        return repository.createProduct(name, description, year)
    }
}