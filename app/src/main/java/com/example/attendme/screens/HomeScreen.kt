package com.example.attendme.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
