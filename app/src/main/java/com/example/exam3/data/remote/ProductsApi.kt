package com.example.exam3.data.remote

import com.example.exam3.data.dto.ProductDto

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductsApi {
    @GET("rest/v1/product")
    suspend fun getProducts(): Response<List<ProductDto>>


    @POST("rest/v1/product")
    suspend fun createProduct(
        @Body product: Map<String, String>
    ): Response<Void>


    @PATCH("rest/v1/product")
    suspend fun updateProduct(
        @Query("id") id: String, // Для Supabase: eq.{id}
        @Body product: Map<String, String>
    ): Response<Void>
}