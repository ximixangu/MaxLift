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
import com.maxlift.domain.usecase.register.RegisterCredentials
import com.maxlift.domain.usecase.register.RegisterUseCase
import com.maxlift.presentation.ui.common.PasswordTextField

@Composable
fun UserRegisterForm(registerUseCase: RegisterUseCase?, navController: NavController) {
    var registerCredentials by remember { mutableStateOf(RegisterCredentials()) }
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
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    )
                )
            }
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                PasswordTextField(
                    value = registerCredentials.password,
                    onValueChange = { data ->
                        registerCredentials = registerCredentials.copy(password = data)
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
                                keyboardController?.hide()
                                navController.navigate("login")
                            }else {
                                Toast.makeText(context, "Error Registering", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                }
            }
        }
    }
}