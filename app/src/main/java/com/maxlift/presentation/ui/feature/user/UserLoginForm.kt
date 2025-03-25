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
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maxlift.domain.usecase.login.Credentials
import com.maxlift.domain.usecase.login.LoginUseCase

@Composable
fun UserLoginForm(loginUseCase: LoginUseCase?, navController: NavController) {
    var credentials by remember { mutableStateOf(Credentials()) }
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
                PasswordTextField(
                    value = credentials.password,
                    onValueChange = { data ->
                        credentials = credentials.copy(password = data)
                    }
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