package com.example.attendme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.example.attendme.navigation.SetUpNavGraph
import com.example.attendme.ui.theme.AttendmeTheme
import java.io.File
import java.io.FileWriter
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttendmeTheme {
                // A surface container using the 'background' color from the theme
                val navHostController = rememberNavController()
                val lifecycleOwner = LocalLifecycleOwner.current
                val context = LocalContext.current
                val path = externalCacheDir?.absolutePath + "/MyAppFolder"
                SetUpNavGraph(navHostController = navHostController, context = context,lifecycleOwner,path)
            }
        }
    }
    private fun writeDataToCsv(data: String) {
        try {
            val folderPath = externalCacheDir?.absolutePath + "/MyAppFolder"
            val folder = File(folderPath)
            if (!folder.exists()) {
                folder.mkdirs()
            }

            val filePath = "$folderPath/data.csv"
            val file = File(filePath)

            val fileWriter = FileWriter(file)
            fileWriter.append(data)
            fileWriter.flush()
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}