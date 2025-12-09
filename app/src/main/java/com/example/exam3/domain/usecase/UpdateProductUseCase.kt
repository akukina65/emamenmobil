package com.example.exam3.domain.usecase

import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.model.Product
import com.example.exam3.domain.repository.ProductsRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(
        id: String,
        name: String,
        description: String,
        year: Int
    ): AuthResult<Product> {
        return repository.updateProduct(id, name, description, year)
    }
}