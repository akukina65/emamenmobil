package com.example.exam3.presentation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun navigate()
{
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController)
        }
        composable("main") {
            ProductsScreen(navController)
        }
        // Экран создания продукта
        composable("createProduct") {
            CreateProductScreen(navController)
        }
    }
}