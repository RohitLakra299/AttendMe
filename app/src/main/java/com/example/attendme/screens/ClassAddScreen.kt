package com.example.attendme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendme.model.Department
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.ClassAddScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassAddScreen(navHostController: NavHostController) {
    val viewModel : ClassAddScreenViewModel = viewModel()
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Select department") }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
            OutlinedTextField(
                value = viewModel.className.value,
                label = { Text(text = "Class Name") },
                onValueChange = {
                    viewModel.className.value = it
                },
                modifier = Modifier.padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = viewModel.batch.value,
                label = { Text(text = "Batch") },
                onValueChange = {
                    viewModel.batch.value = it
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

            Box(modifier = Modifier.fillMaxSize()) {
                ElevatedButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(5.dp),
                    onClick = {viewModel.createClass(
                        onSuccess = {navHostController.navigate(Screen.HomeScreen.route){
                            popUpTo(
                                Screen.LoginScreen.route,
                            ) { inclusive = true }
                        }}
                    , onFailure = {
                            Toast.makeText(
                                context,
                                "Some error: $it",
                                Toast.LENGTH_LONG,
                            ).show()
                        }
                    )
                        },
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) {
                    Text(text = "Continue", modifier = Modifier
                        .padding(8.dp)
                        .width(IntrinsicSize.Max))
                }
            }

        }
    }
}