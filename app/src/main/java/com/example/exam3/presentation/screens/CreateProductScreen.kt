package com.example.exam3.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.exam3.presentation.viewmodel.CreateProductViewModel
import kotlinx.coroutines.delay
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen(
    navController: NavController,
    viewModel: CreateProductViewModel = hiltViewModel()
) {
    val productName by viewModel.productName
    val error by viewModel.error
    val isLoading by viewModel.isLoading
    val isSuccess by viewModel.isSuccess

    // Автоматический возврат при успехе
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            delay(1000) // Ждем секунду
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar( // Теперь можно использовать TopAppBar с аннотацией
                title = { Text("Новый продукт") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Форма
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Поле ввода
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { viewModel.updateName(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Название") },
                        placeholder = { Text("Введите название продукта") },
                        singleLine = true,
                        isError = error != null
                    )

                    // Ошибка
                    if (error != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Ошибка: $error",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Кнопка
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.createProduct() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && productName.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Создать")
                        }
                    }

                    // Сообщение об успехе
                    if (isSuccess) {
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "✓ Продукт создан!",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}