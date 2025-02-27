package com.maxlift.presentation.ui.feature.user

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maxlift.domain.usecase.login.Credentials
import com.maxlift.domain.usecase.login.LoginUseCase

@Composable
fun UserLoginForm(loginUseCase: LoginUseCase?, navController: NavController) {
    var credentials by remember { mutableStateOf(Credentials()) }
    var passwordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.width(300.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                TextField(
                    value = credentials.email,
                    label = { Text(text = "Email") },
                    onValueChange = {
                        data -> credentials = credentials.copy(email = data)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    )
                )
            }
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                TextField(
                    value = credentials.password,
                    label = { Text(text = "Password") },
                    onValueChange = { data ->
                        credentials = credentials.copy(password = data)
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    )
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    Button(
                        modifier = Modifier.width(200.dp),
                        content = { Text(text = "Login") },
                        enabled = credentials.isValid(),
                        onClick = {
                            if(loginUseCase!!.execute(credentials)) {
                                keyboardController?.hide()
                                navController.navigate("menu")
                            } else {
                                Toast.makeText(context, "Error Login", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                Row {
                    Button(
                        modifier = Modifier.width(200.dp),
                        content = { Text(text = "Register") },
                        onClick = {
                            keyboardController?.hide()
                            navController.navigate("register")
                        }
                    )
                }
            }
        }
    }
}