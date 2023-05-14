package com.example.attendme.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val passwordVisible1 by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Select department") }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val image = painterResource(id = R.drawable.register_page_removebg_preview)

    Surface {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(image, "")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .clip(
                        RoundedCornerShape(
                            topStart = 30.dp,
                            topEnd = 30.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sign Up",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        letterSpacing = 2.sp
                    )
                )
                Spacer(modifier = Modifier.padding(12.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = viewModel.name.value,
                        label = { Text(text = "Name") },
                        onValueChange = {
                            viewModel.name.value = it
                        },
                        modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.8f),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Text),
                        keyboardActions = KeyboardActions(onNext = {
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        })
                    )
                    OutlinedTextField(
                        value = viewModel.email.value,
                        label = { Text(text = "Email") },
                        onValueChange = {
                            viewModel.email.value = it
                        },
                        modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.8f).focusRequester(focusRequester = focusRequester),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email)
                    )

                    ExposedDropdownMenuBox(
                        modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.8f),
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
                        modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.8f),
                                label = { Text(text = "Password") },
                        placeholder = { Text(text = "Minimum 6 chars") },
                        isError = viewModel.password.value.length < 6,
                        onValueChange = {
                            viewModel.password.value = it
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible = !passwordVisible
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_visibility_24),
                                    contentDescription = "",
                                    tint = if (passwordVisible) MaterialTheme.colorScheme.primary else Color.Gray,
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Password)
                    )
                    OutlinedTextField(value = viewModel.rePassword.value,
                        modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(0.8f),
                                label = { Text(text = "Re-Password") },
                        onValueChange = {
                            viewModel.rePassword.value = it
                        },
                        isError = viewModel.password.value != viewModel.rePassword.value,
                        visualTransformation = if (passwordVisible1) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible = !passwordVisible
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_visibility_24),
                                    contentDescription = "",
                                    tint = if (passwordVisible) MaterialTheme.colorScheme.primary else Color.Gray,
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
                        keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()})
                    )
                    ElevatedButton(
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(0.8f)
                            .weight(1f, false),
                        enabled = viewModel.email.value.isNotBlank() && viewModel.password.value.length >= 6 && viewModel.password.value == viewModel.rePassword.value,
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
                                    "Error: $it",
                                    Toast.LENGTH_LONG,
                                ).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(Color.Gray)
                    ) {
                        Text(text = "Sign Up", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Login Instead",
                        modifier = Modifier.clickable(onClick = {
                            navHostController.navigate(Screen.LoginScreen.route) {
                                popUpTo = navHostController.graph.startDestinationId
                                launchSingleTop = true
                            }
                        })
                    )
                }

            }
        }
    }
}