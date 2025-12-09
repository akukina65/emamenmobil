package com.example.exam3.domain.usecase

import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.repository.ProductsRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(id: String): AuthResult<Unit> {
        return repository.deleteProduct(id)
    }
}