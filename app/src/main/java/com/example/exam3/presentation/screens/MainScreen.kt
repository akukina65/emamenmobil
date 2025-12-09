package com.example.exam3.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.exam3.domain.model.Product
import com.example.exam3.presentation.viewmodel.ProductsViewModel
import java.net.URLEncoder

@Composable
fun ProductsScreen(
    navController: NavController,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val products = viewModel.filteredProducts.value
    val loading = viewModel.loading.value
    val error = viewModel.error.value
    val searchText = viewModel.searchQuery.value

    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            product = productToDelete,
            onDismiss = {
                showDeleteDialog = false
                productToDelete = null
            },
            onConfirm = {
                productToDelete?.let { product ->
                    viewModel.deleteProduct(product.id) {
                        productToDelete = null
                    }
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Поиск


        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = searchText,
            onValueChange = { viewModel.updateSearchQuery(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Поиск") },
            singleLine = true
        )

        Spacer(Modifier.height(16.dp))

        if (loading) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Ошибка")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadProducts() }) {
                        Text("Повторить")
                    }
                }
            }
        } else if (products.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Нет данных")
            }
        }  else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(products) { product ->
                SimpleProductCard(
                    product = product,
                    onEditClick = { id, name, description, year ->

                        navController.navigate("editProduct/$id/$name/$description/$year")
                    },
                    onDeleteClick = { productId ->
                        productToDelete = viewModel.findProductById(productId)
                        showDeleteDialog = true
                    }
                )
            }
        }
    }


        // Кнопка добавления
        Button(
            onClick = { navController.navigate("createProduct") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Добавить")
        }
    }
}

@Composable
fun SimpleProductCard(
    product: Product,
    onEditClick: (String, String, String, Int) -> Unit = { _, _, _, _ -> },
    onDeleteClick: (String) -> Unit = { _ -> }
) {
    Card(
        Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.name,
                Modifier
                    .weight(1f)
                    .clickable {
                        onEditClick(product.id, product.name, product.decription, product.year)
                    }
            )

            IconButton(
                onClick = { onDeleteClick(product.id) },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(Icons.Default.Delete, "Удалить")
            }

            IconButton(
                onClick = { onEditClick(product.id, product.name, product.decription, product.year) },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(Icons.Default.Edit, "Изменить")
            }
        }
    }
}