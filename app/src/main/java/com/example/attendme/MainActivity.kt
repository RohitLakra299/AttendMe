package com.example.attendme

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.example.attendme.navigation.SetUpNavGraph
import com.example.attendme.ui.theme.AttendmeTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttendmeTheme {
                // A surface container using the 'background' color from the theme
                val navHostController = rememberNavController()
                val lifecycleOwner = LocalLifecycleOwner.current
                val context = LocalContext.current
                val path = externalCacheDir?.absolutePath + "/attendance"
                SetUpNavGraph(navHostController = navHostController, context = context,lifecycleOwner,path)
            }
        }
    }
}