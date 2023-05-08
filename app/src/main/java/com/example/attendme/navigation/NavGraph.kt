package com.example.attendme.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.attendme.screens.ClassAddScreen
import com.example.attendme.screens.HomeScreen
import com.example.attendme.screens.LoginScreen
import com.example.attendme.screens.RegisterScreen


@Composable
fun SetUpNavGraph(
    navHostController: NavHostController,
    context: Context,
    lifecycleOwner: LifecycleOwner
){
    NavHost(navController = navHostController, startDestination = Screen.LoginScreen.route ){
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navHostController)
        }
        composable(route = Screen.RegisterScreen.route){
            RegisterScreen(navHostController)
        }
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navHostController)
        }
        composable(route = Screen.ClassAddScreen.route){
            ClassAddScreen(navHostController)
        }
    }
}