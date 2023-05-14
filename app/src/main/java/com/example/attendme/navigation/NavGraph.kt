package com.example.attendme.navigation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.attendme.screens.ClassAddScreen
import com.example.attendme.screens.CourseViewScreen
import com.example.attendme.screens.EnrolledStudentsScreen
import com.example.attendme.screens.HomeScreen
import com.example.attendme.screens.LoginScreen
import com.example.attendme.screens.RegisterScreen
import com.example.attendme.screens.SplashScreen
import com.example.attendme.viewModels.CourseViewModel
import com.example.attendme.viewModels.StudentListViewModel


@Composable
fun SetUpNavGraph(
    navHostController: NavHostController,
    context: Context,
    lifecycleOwner: LifecycleOwner
){
    NavHost(navController = navHostController, startDestination = Screen.LoginScreen.route ){
        composable(route = Screen.SplashScreen.route){
            SplashScreen(navHostController = navHostController)
        }
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
        composable(route = Screen.CourseViewScreen.route, arguments = listOf(navArgument("classID"){
            this.type = NavType.StringType
            this.nullable = false
        })){
            val classID = it.arguments!!.getString("classID")
            val viewModel: CourseViewModel = viewModel(initializer = {CourseViewModel(classID!!)})
            CourseViewScreen(viewModel, navHostController)
        }

        composable(route = Screen.EnrolledStudentScreen.route, arguments = listOf(navArgument(name = "classID"){
            type = NavType.StringType
            nullable = false
        })){
            Log.d("@@NavGraph", "enrolled screen executed again")
            val classID = it.arguments!!.getString("classID")!!
            val viewModel: StudentListViewModel = viewModel(initializer = {StudentListViewModel(classID) })
            EnrolledStudentsScreen(viewModel = viewModel)
        }
    }
}