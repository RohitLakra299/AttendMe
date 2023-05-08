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
import com.example.attendme.model.Department
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.RegisterScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navHostController: NavHostController) {
    val viewModel: RegisterScreenViewModel = viewModel()
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordVisible1 by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Select department") }
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = viewModel.name.value,
                label = { Text(text = "Name") },
                onValueChange = {
                    viewModel.name.value = it
                },
                modifier = Modifier.padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = viewModel.email.value,
                label = { Text(text = "Email") },
                onValueChange = {
                    viewModel.email.value = it
                },
                modifier = Modifier.padding(bottom = 10.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = { },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    Department.values().forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.department.value = selectionOption
                                selectedOptionText = selectionOption.name
                                expanded = false
                            },
                            text = {
                                Text(text = selectionOption.name)
                            },
                        )
                    }
                }
            }

            OutlinedTextField(value = viewModel.password.value,
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Minimum 6 chars") },
                isError = viewModel.password.value.length < 6,
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
            OutlinedTextField(value = viewModel.rePassword.value,
                label = { Text(text = "Re-Password") },
                onValueChange = {
                    viewModel.rePassword.value = it
                },
                isError = viewModel.password.value != viewModel.rePassword.value,
                visualTransformation = if (passwordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible1)
                        R.drawable.baseline_visibility_24
                    else R.drawable.baseline_visibility_off_24

                    // Localized description for accessibility services
                    val description = if (passwordVisible1) "Hide password" else "Show password"

                    // Toggle button to hide or display password
                    IconButton(onClick = { passwordVisible1 = !passwordVisible1 }) {
                        Icon(painter = painterResource(id = image), description)
                    }
                })

            ClickableText(
                text = AnnotatedString("Login"),
                onClick = {
                    navHostController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.RegisterScreen.route) {
                            inclusive = true
                        }
                    }
                },
                style = TextStyle(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
            )

            ElevatedButton(
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f, false),
                onClick = {
                    viewModel.register(
                        onSuccess = {
                            navHostController.navigate(Screen.HomeScreen.route) {
                                popUpTo(
                                    Screen.LoginScreen.route,
                                ) { inclusive = true }
                            }
                        },
                    ) {
                        Toast.makeText(
                            context,
                            "Some error: $it",
                            Toast.LENGTH_LONG,
                        ).show()
                    }
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