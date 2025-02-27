package com.maxlift.presentation.ui.feature.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(userViewModel: UserViewModel) {
    val user by userViewModel.user.observeAsState()

    LaunchedEffect(Unit) {
        userViewModel.loadUser()
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .width(300.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(user == null) {
                Text(text = "Loading...")
            }else {
                val userAttributes = listOf(
                    "Username" to user!!.name,
                    "Email" to user!!.email,
                )

                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    Modifier.size(70.dp).graphicsLayer { alpha = 0.5f }
                )

                userAttributes.forEach { (attributeName, attributeValue) ->
                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(text = "$attributeName: $attributeValue")
                    }
                }
            }
        }
    }
}