package com.example.exam3.di

import com.example.exam3.data.remote.AuthApi
import com.example.exam3.data.remote.ProductsApi
import com.example.exam3.data.repository.AuthRepositoruIml
import com.example.exam3.data.repository.ProductsRepositoryImpl
import com.example.exam3.domain.repository.AuthRepository
import com.example.exam3.domain.repository.ProductsRepository
import com.example.exam3.domain.usecase.GetProductsUseCase
import com.example.exam3.domain.usecase.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module

@InstallIn(SingletonComponent::class)
object AuthNodule {

    @Provides
    @Singleton
    fun provideAuthRepository(authApi:AuthApi):AuthRepository
    {
        return AuthRepositoruIml(authApi)
    }
    @Provides
    @Singleton
    fun provideProductsRepository(productsApi: ProductsApi): ProductsRepository {
        return ProductsRepositoryImpl(productsApi)
    }
    @Provides
    @Singleton
    fun provideAuthLoginUseCase(authRepository: AuthRepository):LoginUseCase
    {
        return LoginUseCase(authRepository)
    }


    @Provides
    @Singleton
    fun provideGetProductsUseCase(repository: ProductsRepository): GetProductsUseCase {
        return GetProductsUseCase(repository)
    }
}