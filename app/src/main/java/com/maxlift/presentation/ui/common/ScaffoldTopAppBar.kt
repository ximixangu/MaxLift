package com.maxlift.presentation.ui.common

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.maxlift.domain.usecase.login.LogoutUseCase
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffoldTopAppBar(
    navController: NavController,
    logoutUseCase: LogoutUseCase,
    content: @Composable (PaddingValues) -> Unit,
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val keyboardController = LocalSoftwareKeyboardController.current
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry.value?.destination?.route
    var showNavUpIcon by remember { mutableStateOf(false) }

    LaunchedEffect(currentDestination) {
        delay(200)
        showNavUpIcon = currentDestination != "menu"
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "MaxLift",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = { if (showNavUpIcon) {
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            navController.navigateUp()
                        }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }else{
                    MenuButton(logoutUseCase, navController, context)
                }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        content = { innerPadding ->
            content(innerPadding)
        }
    )
}

@Composable
fun MenuButton(logoutUseCase: LogoutUseCase, navController: NavController, context: Context) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                modifier = Modifier.size(25.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text("Profile") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "") },
                onClick = {
                    expanded = false
                    navController.navigate("profile")
                }
            )
            DropdownMenuItem(
                text = { Text("Log Out") },
                leadingIcon = { Icon(Icons.Filled.Output, contentDescription = "") },
                onClick = {
                    if(logoutUseCase.execute()) {
                        expanded = false
                        Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            HorizontalDivider()

            DropdownMenuItem(
                text = { Text("GitHub") },
                leadingIcon = { Icon(Icons.Filled.Hub, contentDescription = "", Modifier.size(20.dp)) },
                onClick = { openWebPage(context,"https://github.com/ximixangu") }
            )
        }
    }
}
