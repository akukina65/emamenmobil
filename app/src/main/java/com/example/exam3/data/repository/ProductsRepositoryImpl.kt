package com.example.exam3.data.repository

import com.example.exam3.data.remote.ProductsApi
import com.example.exam3.domain.model.AuthResult
import com.example.exam3.domain.model.Product
import com.example.exam3.domain.repository.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi
) : ProductsRepository {

    override suspend fun getProducts(): AuthResult<List<Product>> {
        return try {
            val response = productsApi.getProducts()
            val products = response.body()?.mapNotNull { dto ->
                // Проверяем только обязательные поля (id и name)
                if (dto.id != null && dto.name != null) {
                    // Используем значения по умолчанию для необязательных полей
                    Product(
                        id = dto.id,
                        name = dto.name,
                        decription = dto.decription ?: "",  // пустая строка вместо null
                        year = dto.year ?: 0                // 0 вместо null
                    )
                } else {
                    null
                }
            } ?: emptyList()

            AuthResult.Success(products)
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }

    override suspend fun createProduct(
        name: String,
        description: String,
        year: Int
    ): AuthResult<Product> {
        return try {
            if (name.isBlank()) {
                return AuthResult.Failure(Exception("Введите название"))
            }

            val request = mapOf(
                "name" to name,
                "decription" to description,  // исправил с "description" на "decription"
                "year" to year.toString()
            )

            val response = productsApi.createProduct(request)

            if (response.isSuccessful) {
                val newId = "created_${System.currentTimeMillis()}"
                AuthResult.Success(
                    Product(
                        id = newId,
                        name = name,
                        decription = description,
                        year = year
                    )
                )
            } else {
                AuthResult.Failure(Exception("Не удалось создать продукт"))
            }
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }

    override suspend fun updateProduct(
        id: String,
        name: String,
        description: String,
        year: Int
    ): AuthResult<Product> {
        return try {
            if (name.isBlank()) return AuthResult.Failure(Exception("Введите название"))
            if (id.isBlank()) return AuthResult.Failure(Exception("Ошибка ID"))

            val request = mapOf(
                "name" to name,
                "decription" to description,  // исправил с "description" на "decription"
                "year" to year.toString()
            )

            val response = productsApi.updateProduct("eq.$id", request)

            if (response.isSuccessful) {
                AuthResult.Success(
                    Product(
                        id = id,
                        name = name,
                        decription = description,
                        year = year
                    )
                )
            } else {
                AuthResult.Failure(Exception("Ошибка обновления"))
            }
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }


    override suspend fun deleteProduct(id: String): AuthResult<Unit> {
        return try {
            if (id.isBlank()) return AuthResult.Failure(Exception("Ошибка ID"))

            val response = productsApi.deleteProduct("eq.$id")

            if (response.isSuccessful) {
                AuthResult.Success(Unit)
            } else {
                AuthResult.Failure(Exception("Не удалось удалить продукт: ${response.code()}"))
            }
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }
}