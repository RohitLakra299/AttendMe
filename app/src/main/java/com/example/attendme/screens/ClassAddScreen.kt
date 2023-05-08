package com.example.attendme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.ClassAddScreenViewModel

@Composable
fun ClassAddScreen(navHostController: NavHostController) {
    val viewModel : ClassAddScreenViewModel = viewModel()
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

            Box(modifier = Modifier.fillMaxSize()) {
                ElevatedButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(5.dp),
                    onClick = {viewModel.createClass()
                        if(viewModel.status.value) navHostController.navigate(Screen.HomeScreen.route)},
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