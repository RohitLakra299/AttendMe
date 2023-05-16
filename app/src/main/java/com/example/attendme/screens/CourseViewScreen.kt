package com.example.attendme.screens

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.attendme.viewModels.CourseViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseViewScreen(viewModel: CourseViewModel, navHostController: NavHostController, path: String) {
    val context = LocalContext.current
    Surface {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(12.dp).fillMaxSize()
        ) {
            CourseInfoRow("Course Title", viewModel.currClass.value.className, modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
            CourseInfoRow(param = "Course Code", value = viewModel.classID, modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
            CourseInfoRow("Batch", viewModel.currClass.value.batch, modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
            CourseInfoRow("Department", viewModel.currClass.value.department.toString(), modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
            Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), onClick = { navHostController.navigate("enrolled_student/${viewModel.classID}") }, modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Enrolled Students",
                        modifier = Modifier.weight(6f),
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                    )
                    Text(text = viewModel.currClass.value.noOfStudents.toString(), modifier = Modifier.weight(3f), style = TextStyle(fontSize = 20.sp))
                    IconButton(onClick = {  }, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = "to student list"
                        )
                    }
                }
            }
            OtpBoxAndButtons(viewModel, context, path, Modifier.weight(4f))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun OtpBoxAndButtons(
    viewModel: CourseViewModel,
    context: Context,
    path: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = viewModel.otpValue.value,
                style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Black)
            )
            Text(
                text = "Enter the digits in the website to start generating QR for students to mark their attendance\nDo not share!!",
                style = TextStyle(fontStyle = FontStyle.Italic),
                textAlign = TextAlign.Center
            )

        }
        Row(modifier = Modifier.align(Alignment.BottomCenter), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {

            TextButton(
                onClick = {
                    viewModel.downloadCsv(
                        onSuccess = { Toast.makeText(context, "Attendance downloaded as csv file", Toast.LENGTH_LONG).show()}
                    )
                },
                modifier = Modifier
                    .weight(0.3f)
            ) {
                Text(text = "Download attendance", style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center))
            }
            Spacer(modifier = Modifier.width(12.dp))
            ElevatedButton(
                enabled = !viewModel.isAttendance.value,
                onClick = {
                    if (viewModel.otpValue.value == "******") {
                        viewModel.addOtpAndClassID()
                    } else {
                        Toast.makeText(
                            context,
                            "Some error: OTP value already generated",
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                },
                modifier = Modifier
                    .weight(0.5f)
            ) {
                Text(text = "Start Attendance", style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center))
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CourseInfoRow(param: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = param,
                modifier = Modifier.weight(6f),
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            )
            Text(text = value, modifier = Modifier.weight(4f), style = TextStyle(fontSize = 20.sp))
        }
    }
}

//@Preview(showBackground = true, device = Devices.DEFAULT)
//@Composable
//fun PreviewCourseViewScreen() {
//    CourseViewScreen(classID)
//}