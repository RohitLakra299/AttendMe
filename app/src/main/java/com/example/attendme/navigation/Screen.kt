package com.example.attendme.navigation

sealed class Screen(val route : String) {
    object LoginScreen : Screen("screen_login")
    object RegisterScreen : Screen("screen_register")
    object HomeScreen : Screen("screen_home")
    object ClassAddScreen : Screen("class_add_screen")
}