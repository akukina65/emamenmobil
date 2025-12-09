package com.example.exam3.presentation.screens


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.exam3.domain.model.Product

@Composable
fun DeleteConfirmationDialog(
    product: Product?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (product != null) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Удалить?") },
            text = { Text("Удалить \"${product.name}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                    onDismiss()
                }) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Нет")
                }
            }
        )
    }
}