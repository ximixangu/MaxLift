package com.maxlift.presentation.ui.activities.loginActivity

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.maxlift.domain.usecase.login.Credentials
import com.maxlift.domain.usecase.login.LoginUseCase
import com.maxlift.presentation.ui.activities.menuActivity.MenuActivity

@Composable
fun UserLoginForm(loginUseCase: LoginUseCase?) {
    var credentials by remember { mutableStateOf(Credentials()) }
    val context = LocalContext.current

    Surface {
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
                    }
                )
            }
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                TextField(
                    //TODO: Boto per alternar la visibilitat de la contrasenya
                    visualTransformation = PasswordVisualTransformation(),
                    value = credentials.password,
                    label = { Text(text = "Password") },
                    onValueChange = {
                            data -> credentials = credentials.copy(password = data)
                    }
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    Button(
                        modifier = Modifier.width(200.dp),
                        content = { Text(text = "Login") },
                        enabled = credentials.isNotEmpty(),
                        onClick = {
                            if(loginUseCase!!.execute(credentials)) {
                                val intent = Intent()
                                intent.setClass(context, MenuActivity::class.java)
                                context.startActivity(intent)
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
                        onClick = { /*TODO*/ }
                    )
                }
            }
        }
    }
}