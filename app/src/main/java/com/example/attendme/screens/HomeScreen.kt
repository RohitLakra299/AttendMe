package com.example.attendme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendme.model.ClassModel
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.HomeScreenViewModel

@Composable
fun HomeScreen(navHostController: NavHostController){
    val viewModel : HomeScreenViewModel = viewModel()
    Surface(modifier = Modifier.fillMaxSize()) {
        LaunchedEffect(true){
            viewModel.getClasses()
        }
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            LazyColumn(){
                items(items = viewModel.classesList.value){
                    ClassCard(classModel = it)
                }
            }
            Box(contentAlignment = Alignment.Center) {
                ElevatedButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(5.dp),
                    onClick = {navHostController.navigate(Screen.ClassAddScreen.route)},
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) {
                    Text(text = "+", modifier = Modifier
                        .padding(8.dp)
                        .width(IntrinsicSize.Max))
                }
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassCard(classModel: ClassModel){
    Card(modifier = Modifier.fillMaxWidth(), shape = RectangleShape) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = classModel.className, modifier = Modifier.padding(10.dp))
            Text(text = classModel.batch, modifier = Modifier.padding(10.dp))
        }
    }
}
