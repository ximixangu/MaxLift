package com.maxlift.presentation.ui.activities.registerActivity

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.maxlift.domain.usecase.register.RegisterCredentials
import com.maxlift.domain.usecase.register.RegisterUseCase
import com.maxlift.presentation.ui.activities.loginActivity.LoginActivity

@Composable
fun UserRegisterForm(registerUseCase: RegisterUseCase?) {
    var registerCredentials by remember { mutableStateOf(RegisterCredentials()) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface {
        Column(
            modifier = Modifier.width(300.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                TextField(
                    value = registerCredentials.name,
                    label = { Text(text = "Username") },
                    onValueChange = {
                            data -> registerCredentials = registerCredentials.copy(name = data)
                    }
                )
            }
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                TextField(
                    value = registerCredentials.email,
                    label = { Text(text = "Email") },
                    onValueChange = {
                            data -> registerCredentials = registerCredentials.copy(email = data)
                    }
                )
            }
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                TextField(
                    value = registerCredentials.password,
                    label = { Text(text = "Password") },
                    onValueChange = { data ->
                        registerCredentials = registerCredentials.copy(password = data)
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    }
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Row {
                    Button(
                        modifier = Modifier.width(200.dp),
                        content = { Text(text = "Register") },
                        enabled = registerCredentials.isValid(),
                        onClick = {
                            if (registerUseCase!!.execute(registerCredentials)) {
                                val intent = Intent()
                                intent.setClass(context, LoginActivity::class.java)
                                context.startActivity(intent)
                                println("bello")
                            }else {
                                println("pells")
                                Toast.makeText(context, "Error Registering", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                }
            }
        }
    }
}