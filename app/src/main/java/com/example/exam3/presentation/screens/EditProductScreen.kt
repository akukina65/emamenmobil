// EditProductScreen.kt
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
import com.example.exam3.presentation.viewmodel.EditProductViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(navController: NavController) {
    val viewModel = hiltViewModel<EditProductViewModel>()
    val productName by viewModel.productName
    val error by viewModel.error
    val isLoading by viewModel.isLoading
    val isSuccess by viewModel.isSuccess

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            delay(1000)
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактировать") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { viewModel.updateName(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Название") },
                        singleLine = true,
                        isError = error != null
                    )

                    if (error != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(error!!, color = MaterialTheme.colorScheme.error)
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.updateProduct() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && productName.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(Modifier.size(20.dp))
                        } else {
                            Text("Сохранить")
                        }
                    }

                    if (isSuccess) {
                        Spacer(Modifier.height(16.dp))
                        Text("✓ Сохранено!", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}