package com.example.exam3.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.exam3.presentation.viewmodel.AuthViewModel

@Composable

fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
)
{
    var email by  remember { mutableStateOf("") }
    var password by  remember { mutableStateOf("") }

    val authState by viewModel.authState
    val loading by viewModel.loading
    val error by viewModel.error



    LaunchedEffect(authState) {
        if(authState!=null)
        {
            navController.navigate("main")
            {
                popUpTo("login") {inclusive = true}
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text("Добро пожаловать!",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = email,
            onValueChange = {email = it},
            label = { Text("Email")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = password,
            onValueChange = {password = it},
            label = { Text("Пароль")},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = MaterialTheme.shapes.medium)

        Spacer(Modifier.height(16.dp))

        Button(onClick = {viewModel.login(email,password)},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Green,
                containerColor = Color.Magenta
            ),
            shape = MaterialTheme.shapes.large,
            enabled = !loading



            ) {

            if(loading)
            {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp)
                )
            }
            else{
                Text("Войти")
            }
            if(error!=null)
            {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

        }

    }
}