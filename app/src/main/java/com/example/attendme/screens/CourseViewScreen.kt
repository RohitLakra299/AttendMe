package com.example.attendme.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseViewScreen(viewModel: CourseViewModel, navHostController: NavHostController, path: String) {
    val context = LocalContext.current
    Surface {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 12.dp)
        ) {
            CourseInfoRow("Course Title", viewModel.currClass.value.className, modifier = Modifier.fillMaxWidth())
            CourseInfoRow(param = "Course Code", value = viewModel.classID)
            CourseInfoRow("Batch", viewModel.currClass.value.batch, modifier = Modifier.fillMaxWidth())
            CourseInfoRow("Department", viewModel.currClass.value.department.toString(), modifier = Modifier.fillMaxWidth())
            Card(elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Enrolled Students",
                        modifier = Modifier.weight(6f),
                        style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                    )
                    Text(text = viewModel.currClass.value.noOfStudents.toString(), modifier = Modifier.weight(3f), style = TextStyle(fontSize = 20.sp))
                    IconButton(onClick = { navHostController.navigate("enrolled_student/${viewModel.classID}") }, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = "to student list"
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
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
            ElevatedButton(
                enabled = !viewModel.isAttendance.value,
                onClick = {
                          if(viewModel.otpValue.value == "******"){
                                viewModel.addOtpAndClassID()
                          }else{
                              Toast.makeText(
                                  context,
                                  "Some error: OTP value already generated",
                                  Toast.LENGTH_LONG,
                              ).show()
                          }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 12.dp)
            ) {
                Text(text = "Start Attendance for this class")
            }
            ElevatedButton(
                onClick = {
                        viewModel.downloadCsv(
                            onSuccess = {
                                val file = File("$path/attendance.csv")

                                // Create a new Intent to download the file
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setDataAndType(Uri.fromFile(file), "text/csv")
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

                                // Start the download
                                try {
                                    context.startActivity(intent)
                                } catch (e: ActivityNotFoundException) {
                                    Toast.makeText(context, "No app found to open CSV files", Toast.LENGTH_LONG).show()
                                }
                            }
                        )
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 12.dp)
            ) {
                Text(text = "Download Attendance.csv")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CourseInfoRow(param: String, value: String, modifier: Modifier = Modifier) {
    Card() {
        Row(modifier = modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
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