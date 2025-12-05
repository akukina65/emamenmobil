package com.example.exam3.di

import com.example.exam3.data.remote.AuthApi
import com.example.exam3.data.remote.AuthApiService
import com.example.exam3.data.remote.ProductsApi
import com.example.exam3.data.remote.ProductsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)

object NetworkModul {

    @Provides
    @Singleton
    fun provadesauthApi():AuthApi
    {
        return AuthApiService.create()
    }


    @Provides
    @Singleton
    fun provideProductsApi(): ProductsApi {
        return ProductsApiService.create()
    }
}