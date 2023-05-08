package com.example.attendme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.attendme.model.ClassModel
import com.example.attendme.navigation.Screen

@Composable
fun HomeScreen(navHostController: NavHostController){
    val classList = ArrayList<ClassModel>()
    Surface(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {
            ElevatedButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp),
                onClick = {navHostController.navigate(Screen.ClassAddScreen.route)},
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Text(text = "Continue", modifier = Modifier
                    .padding(8.dp)
                    .width(IntrinsicSize.Max))
            }
        }
    }
}
