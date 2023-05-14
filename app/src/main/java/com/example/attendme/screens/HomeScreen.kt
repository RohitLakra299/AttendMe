package com.example.attendme.screens

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendme.R
import com.example.attendme.model.ClassModel
import com.example.attendme.navigation.Screen
import com.example.attendme.viewModels.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    val viewModel: HomeScreenViewModel = viewModel()
    LaunchedEffect(true) {
        viewModel.getClasses()
    }
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Attend.Me-Teacher") },
                actions = {
                    IconButton(onClick = {
                        viewModel.signOut()
                        navHostController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.LoginScreen.route) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "sign out"
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors()
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { navHostController.navigate(Screen.ClassAddScreen.route) }) {
                Text(text = "New Class")
            }
        }
    ) { paddingValues ->
        Surface(color = MaterialTheme.colorScheme.surface) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(paddingValues)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(72.dp),
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    colorFilter = ColorFilter.tint(Color.Magenta),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = viewModel.professor.value.name,
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = viewModel.professor.value.department.toString(),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Your active classes >", modifier = Modifier.fillMaxWidth())
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = viewModel.classesList.value) { classModel ->
                        ClassCard(classModel = classModel) {
                            navHostController.navigate("course_view_screen/${it}")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassCard(classModel: ClassModel, onClick: (String) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = { onClick(classModel.classId) }),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = classModel.className, modifier = Modifier.padding(10.dp))
            Text(text = classModel.batch, modifier = Modifier.padding(10.dp))
        }
    }
}
