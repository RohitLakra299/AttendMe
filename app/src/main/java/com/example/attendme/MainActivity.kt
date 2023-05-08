package com.example.attendme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.attendme.navigation.SetUpNavGraph
import com.example.attendme.ui.theme.AttendmeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttendmeTheme {
                // A surface container using the 'background' color from the theme
                val navHostController = rememberNavController()
                val lifecycleOwner = LocalLifecycleOwner.current
                val context = LocalContext.current
                SetUpNavGraph(navHostController = navHostController, context = context,lifecycleOwner)
            }
        }
    }
}