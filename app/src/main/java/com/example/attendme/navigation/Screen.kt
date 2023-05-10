package com.example.attendme.navigation

sealed class Screen(val route : String) {
    object LoginScreen : Screen("screen_login")
    object RegisterScreen : Screen("screen_register")
    object HomeScreen : Screen("screen_home")
    object ClassAddScreen : Screen("class_add_screen")
    object CourseViewScreen : Screen("course_view_screen/{classID}")
    object EnrolledStudentScreen : Screen("enrolled_student/{classID}")
}