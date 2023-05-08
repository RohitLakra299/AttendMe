package com.example.attendme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendme.R
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.LoginScreenViewModel


@Composable
fun LoginScreen(navHostController: NavHostController){
    val viewModel : LoginScreenViewModel = viewModel()
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
            OutlinedTextField(
                value = viewModel.email.value,
                label = { Text(text = "Email") },
                onValueChange = {
                    viewModel.email.value = it
                },
                modifier = Modifier.padding(bottom = 10.dp)
            )

            OutlinedTextField(value = viewModel.password.value,
                label = { Text(text = "Password") },
                onValueChange = {
                    viewModel.password.value = it
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        R.drawable.baseline_visibility_24
                    else R.drawable.baseline_visibility_off_24

                    // Localized description for accessibility services
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    // Toggle button to hide or display password
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(id = image), description)
                    }
                })
            ClickableText(
                text = AnnotatedString("or Register"),
                onClick = { navHostController.navigate(Screen.RegisterScreen.route){
                    popUpTo(route = Screen.LoginScreen.route){
                        inclusive = true
                    }
                } },
                style = TextStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
            )
            Box(modifier = Modifier.fillMaxSize()) {
                ElevatedButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(5.dp),
                    onClick = {
                        viewModel.login(
                            onSuccess = {
                                navHostController.navigate(Screen.HomeScreen.route) {
                                    popUpTo(
                                        Screen.LoginScreen.route,
                                    ) { inclusive = true }
                                }
                            },
                            onFailure = {
                                Toast.makeText(
                                    context,
                                    "Some error: $it",
                                    Toast.LENGTH_LONG,
                                ).show()
                            },
                        )
                    },
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) {
                    Text(
                        text = "Continue", modifier = Modifier
                            .padding(8.dp)
                            .width(IntrinsicSize.Max)
                    )
                }
            }
        }
    }

}