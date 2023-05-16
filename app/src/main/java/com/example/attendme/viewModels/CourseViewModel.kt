package com.example.attendme.viewModels


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendme.model.AttendanceModel
import com.example.attendme.model.ClassModel
import com.example.attendme.model.Department
import com.example.attendme.model.OtpModel
import com.example.attendme.model.StudentAttendanceModel
import com.example.attendme.model.StudentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(val classID: String, val path: String): ViewModel() {
    private val db = Firebase.firestore.collection("Classes")
    private val studentDb = Firebase.firestore.collection("Students")
    private val otpDb = Firebase.firestore.collection("CurrentLiveAttendance")
    private val auth = FirebaseAuth.getInstance()
    var currClass = mutableStateOf(ClassModel("", classID, "", "", Department.NONE, 0))
    val otpValue = mutableStateOf("******")
    val isAttendance = mutableStateOf(false)
    var studentsList = mutableStateOf<List<StudentModel>>(emptyList())

    init {
        getClassDetails()
        getCurrOtp()
        otpListener()

    }

    fun getClassDetails() = CoroutineScope(Dispatchers.IO).launch {
        val classQuery = db.whereEqualTo("classId", classID).get().await()
        if (classQuery.documents.isNotEmpty()) {
            for (doc in classQuery) {
                val dep = doc.get("department").toString()
                var department: Department
                if (dep == Department.CS_DEPARTMENT.toString()) {
                    department = Department.CS_DEPARTMENT
                } else if (dep == Department.IT_DEPARTMENT.toString()) {
                    department = Department.IT_DEPARTMENT
                } else if (dep == Department.MECH_DEPARTMENT.toString()) {
                    department = Department.MECH_DEPARTMENT
                } else {
                    department = Department.NONE
                }
                val classes = ClassModel(
                    auth.uid!!,
                    doc.get("classId").toString(),
                    doc.get("className").toString(),
                    doc.get("batch").toString(),
                    department,
                    doc.get("noOfStudents").toString().toInt()
                )
                currClass.value = classes
            }
        }
    }

    fun addOtpAndClassID() = CoroutineScope(Dispatchers.IO).launch {
        val otpQuery = otpDb.whereEqualTo("classId", classID).get().await()
        if (otpQuery.documents.isNotEmpty()) {
            Log.d("@@addOtpAndClassID", "Class attendance already in progress")

        } else {
            val alphanumeric = ('0'..'9')
            otpValue.value = (1..6).map { alphanumeric.random() }.joinToString("")
            val otpModel = OtpModel(otpValue.value, classID)
            otpDb.add(otpModel).await()
            isAttendance.value = true
        }
    }

    fun getCurrOtp() = CoroutineScope(Dispatchers.IO).launch {
        val otpQuery = otpDb.whereEqualTo("classId", classID).get().await()
        if (otpQuery.documents.isNotEmpty()) {
            for (doc in otpQuery) {
                otpValue.value = doc.get("otp").toString()
            }
        }
    }

    private fun otpListener() = CoroutineScope(Dispatchers.IO).launch {
        otpDb.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle any errors that occur.
                return@addSnapshotListener
            }
            snapshot?.documentChanges?.forEach { change ->
                if (change.type == DocumentChange.Type.REMOVED) {
                    val deletedDocument = change.document
                    otpValue.value = "******"
                    isAttendance.value = false
                    Log.d("MyViewModel", "Document ${deletedDocument.id} was deleted.")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadCsv(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val attendanceList = mutableStateOf<List<AttendanceModel>>(emptyList())
            val totalDates = mutableListOf<String>()
            val studentQuery = studentDb.get().await()
            if (studentQuery.documents.isNotEmpty()) {
                var studentClassIDList: MutableList<String>
                val studentList = mutableListOf<StudentModel>()
                var currStudent: StudentModel
                for (studentDoc in studentQuery) {
                    studentClassIDList = studentDoc.get("classes") as MutableList<String>
                    if (classID in studentClassIDList) {
                        currStudent = StudentModel(
                            studentDoc.get("id").toString(),
                            studentDoc.get("name").toString(),
                            studentDoc.get("email").toString(),
                            studentDoc.get("rollNo").toString(),
                            studentClassIDList
                        )
                        studentList.add(currStudent)
                    }
                }
                studentsList.value = studentList
                val currAttendanceList = mutableListOf<AttendanceModel>()
                for (data in studentsList.value) {
                    currAttendanceList.add(
                        AttendanceModel(
                            emptyList(),
                            data.name,
                            data.rollNo,
                            data.id
                        )
                    )
                }
                attendanceList.value = currAttendanceList
            }
            Log.d("@@GetAll", attendanceList.value.toString())
            Log.d("@@downloadCsv0", currClass.value.classId)

            val classQuery = db.whereEqualTo("classId", currClass.value.classId).get().await()
            Log.d("@@downloadCsv1", attendanceList.value.toString())
            if (classQuery.documents.isNotEmpty()) {
                Log.d("@@downloadCsv2", attendanceList.value.toString())
                for (classDoc in classQuery) {
                    val attendanceDb =
                        Firebase.firestore.collection("Classes/${classDoc.id}/Attendance")
                    val attendanceQuery = attendanceDb.get().await()
                    for (perDayAttendanceDoc in attendanceQuery) {
                        Log.d("@@downloadCsv3", attendanceList.value.toString())
                        val date = perDayAttendanceDoc.get("date").toString()
                        totalDates.add(date)
                        val studentList =
                            perDayAttendanceDoc.get("studentList") as? List<Map<String, Any>>
                        val mappedStudentList = studentList?.map {
                            StudentAttendanceModel(
                                it["id"] as String,
                                it["studentName"] as String,
                                it["time"] as String
                            )
                        }?.toMutableList()
                        for (attendance in attendanceList.value) {
                            val dateList = mutableListOf<String>()
                            dateList.addAll(attendance.date)
                            for (student in mappedStudentList!!) {
                                if (student.id == attendance.id) {
                                    dateList.add(date)
                                    attendance.date = dateList
                                    break
                                }
                            }
                        }
                    }
                }
                Log.d("@@downloadCsv", attendanceList.value.toString())
            }

            var dataToWrite = "Id, Name, RollNo, " + totalDates.joinToString()
            for (item in attendanceList.value) {
                val row = ArrayList<String>()
                row.ensureCapacity(totalDates.size + 3)
                row.add(item.id)
                row.add(item.name)
                row.add(item.rollNumber)
                for (date in totalDates) {
                    var toAdd = "0"
                    if (date in item.date)
                        toAdd = "1"
                    row.add(toAdd)
                }
                dataToWrite = dataToWrite.plus("\n${row.joinToString()}")
            }
            writeDataToCsv(dataToWrite)
            onSuccess()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun writeDataToCsv(data: String) {
        Log.d("@@csv", data)
        try {
            val folderPath = path
            val folder = File(folderPath)
            if (!folder.exists()) {
                folder.mkdirs()
            }

            val filePath = "$folderPath/${currClass.value.className}-${LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))}.csv"
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