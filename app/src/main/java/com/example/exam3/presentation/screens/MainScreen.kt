package com.example.exam3.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.exam3.domain.model.Product
import com.example.exam3.presentation.viewmodel.ProductsViewModel

@Composable
fun ProductsScreen(
    navController: NavController,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val products = viewModel.filteredProducts.value
    val loading = viewModel.loading.value
    val error = viewModel.error.value
    val searchText = viewModel.searchQuery.value

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            text = "Продукты",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(16.dp))

        // Поле поиска
        OutlinedTextField(
            value = searchText,
            onValueChange = { viewModel.updateSearchQuery(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Поиск...") },
            leadingIcon = { Icon(Icons.Default.Search, "Поиск") },
            trailingIcon = {
                if (searchText.isNotBlank()) {
                    IconButton(onClick = { viewModel.clearSearch() }) {
                        Icon(Icons.Default.Close, "Очистить")
                    }
                }
            },
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        // Проверка состояний
        if (loading) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(error, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(16.dp))
                Button(onClick = { viewModel.loadProducts() }) {
                    Text("Повторить")
                }
            }
        } else if (products.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                if (searchText.isNotBlank()) {
                    Text("Ничего не найдено")
                } else {
                    Text("Нет данных")
                }
            }
        } else {
            // Список продуктов
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                // ProductsScreen.kt - в LazyColumn
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onEditClick = { id, name -> // ← Поменяли onClick на onEditClick
                            navController.navigate("editProduct/${id}/${name}")
                        }
                    )
                }
            }
        }

        // Обычная кнопка внизу
        Button(
            onClick = {
                navController.navigate("createProduct") {
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Добавить",
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("Добавить продукт")
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onEditClick: (String, String) -> Unit = { _, _ -> }
) {
    Card(
        Modifier
            .fillMaxWidth()
            .clickable { onEditClick(product.id, product.name) }
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(product.name, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}