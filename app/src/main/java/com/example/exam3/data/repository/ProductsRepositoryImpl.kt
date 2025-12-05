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
            //if (response.isSuccessful) {
                val products = response.body()?.mapNotNull { dto ->
                    if (dto.id != null && dto.name != null) {
                        Product(id = dto.id, name = dto.name)
                    } else {
                        null
                    }
                } ?: emptyList()

                AuthResult.Success(products)
            //} else {
               // AuthResult.Failure(Exception("Ошибка сервера: ${response.code()}"))
            //}
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }




    override suspend fun createProduct(name: String): AuthResult<Product> {
        return try {
            if (name.isBlank()) {
                return AuthResult.Failure(Exception("Введите название"))
            }

            val request = mapOf("name" to name)
            val response = productsApi.createProduct(request)

            if (response.isSuccessful) {
                // Просто считаем что успешно создано
                AuthResult.Success(
                    Product(id = "created_${System.currentTimeMillis()}", name = name)
                )
            } else {
                AuthResult.Failure(Exception("Не удалось создать продукт"))
            }
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }








    // ProductsRepositoryImpl.kt - добавьте этот метод
    override suspend fun updateProduct(id: String, name: String): AuthResult<Product> {
        return try {
            if (name.isBlank()) return AuthResult.Failure(Exception("Введите название"))
            if (id.isBlank()) return AuthResult.Failure(Exception("Ошибка ID"))

            val request = mapOf("name" to name)
            val response = productsApi.updateProduct("eq.$id", request)

            if (response.isSuccessful) {
                AuthResult.Success(Product(id = id, name = name))
            } else {
                AuthResult.Failure(Exception("Ошибка обновления"))
            }
        } catch (e: Exception) {
            AuthResult.Failure(e)
        }
    }
}