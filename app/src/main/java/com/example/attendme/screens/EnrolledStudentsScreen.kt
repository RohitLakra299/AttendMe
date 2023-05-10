package com.example.attendme.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendme.viewModels.StudentListViewModel

@Composable
fun  EnrolledStudentsScreen(viewModel: StudentListViewModel) {
    LaunchedEffect(key1 = true, block = { viewModel.getAllStudent() })
    Column(modifier = Modifier.fillMaxSize().padding(top = 20.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total Students",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 28.sp,
                modifier = Modifier.padding(16.dp).weight(1f)
            )
            Text(
                text = viewModel.studentsList.value.size.toString(),
                fontSize = 28.sp,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp).weight(1f)
            )
        }
        LazyColumn(){
            item {
                StudentInfoRow(param = "Name", value = "Roll No")
            }
            items(items = viewModel.studentsList.value){
                StudentInfoRow(param = it.name, value =it.rollNo)
            }
        }
    }
}

@Composable
fun StudentInfoRow(param: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = param,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}
