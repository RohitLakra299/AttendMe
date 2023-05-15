package com.example.attendme.viewModels


import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
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
import com.opencsv.CSVWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(val classID: String, val path: File): ViewModel(){
    private val db = Firebase.firestore.collection("Classes")
    private val studentDb = Firebase.firestore.collection("Students")
    private val otpDb = Firebase.firestore.collection("CurrentLiveAttendance")
    private val auth = FirebaseAuth.getInstance()
    var currClass = mutableStateOf(ClassModel("",classID,"","",Department.NONE,0))
    val otpValue = mutableStateOf("******")
    val isAttendance = mutableStateOf(false)
    var studentsList = mutableStateOf<List<StudentModel>>(emptyList())
    init {
        getClassDetails()
        getCurrOtp()
        otpListener()

    }
    fun getClassDetails() = CoroutineScope(Dispatchers.IO).launch {
        val classQuery = db.whereEqualTo("classId",classID).get().await()
        if(classQuery.documents.isNotEmpty()){
            for(doc in classQuery){
                var dep = doc.get("department").toString()
                var department : Department
                if(dep == Department.CS_DEPARTMENT.toString()){
                    department = Department.CS_DEPARTMENT
                }else if(dep == Department.IT_DEPARTMENT.toString()){
                    department = Department.IT_DEPARTMENT
                }else if(dep == Department.MECH_DEPARTMENT.toString()){
                    department = Department.MECH_DEPARTMENT
                }else{
                    department = Department.NONE
                }
                var classes = ClassModel(auth.uid!!,doc.get("classId").toString(),doc.get("className").toString(),doc.get("batch").toString(),department,doc.get("noOfStudents").toString().toInt())
                currClass.value = classes
            }
        }
    }
    fun addOtpAndClassID() = CoroutineScope(Dispatchers.IO).launch {
        val otpQuery = otpDb.whereEqualTo("classId",classID).get().await()
        var check = true
        if(otpQuery.documents.isNotEmpty()){
            Log.d("@@addOtpAndClassID", "Class attendance already in progress")

        }else{
                val alphanumeric = ('0'..'9')
                otpValue.value = (1..6).map { alphanumeric.random() }.joinToString("")
                val otpModel = OtpModel(otpValue.value,classID)
                otpDb.add(otpModel).await()
                isAttendance.value = true
        }
    }
    fun getCurrOtp() = CoroutineScope(Dispatchers.IO).launch{
        val otpQuery = otpDb.whereEqualTo("classId",classID).get().await()
        if(otpQuery.documents.isNotEmpty()){
            for(doc in otpQuery){
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

    fun downloadCsv(onSuccess: () -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        var attendanceList = mutableStateOf<List<AttendanceModel>>(emptyList())
        var totalDates = mutableListOf<String>()
        val studentQuery = studentDb.get().await()
        if(studentQuery.documents.isNotEmpty()){
            var idList: MutableList<String>
            var studentList = mutableListOf<StudentModel>()
            var currStudent : StudentModel
            for(doc in studentQuery){
                idList =  doc.get("classes") as MutableList<String>
                for(value in idList){
                    if(value == classID){
                        currStudent = StudentModel(doc.get("id").toString(),doc.get("name").toString(),doc.get("email").toString(),doc.get("rollNo").toString(),idList)
                        studentList.add(currStudent)
                        break
                    }
                }
            }
            studentsList.value = studentList
            var currAttendanceList = mutableListOf<AttendanceModel>()
            for(data in studentsList.value){
                currAttendanceList.add(AttendanceModel(emptyList(),data.name,data.rollNo,data.id))
            }
            attendanceList.value = currAttendanceList
        }
        Log.d("@@GetAll", attendanceList.value.toString())
        Log.d("@@downloadCsv0", currClass.value.classId.toString())

        val classQuery = db.whereEqualTo("classId",currClass.value.classId).get().await()
        Log.d("@@downloadCsv1", attendanceList.value.toString())
        if(classQuery.documents.isNotEmpty()){
            Log.d("@@downloadCsv2", attendanceList.value.toString())
            for(doc in classQuery){
                val attendanceDb = Firebase.firestore.collection("Classes/${doc.id}/Attendance")
                val attendanceQuery = attendanceDb.get().await()
                for(docs in attendanceQuery){
                    Log.d("@@downloadCsv3", attendanceList.value.toString())
                    val date = docs.get("date").toString()
                    totalDates.add(date)
                    val studentList = docs.get("studentList") as? List<Map<String, Any>>
                    val mappedStudentList = studentList?.map {
                        StudentAttendanceModel(
                            it["id"] as String,
                            it["studentName"] as String,
                            it["time"] as String
                        )
                    }?.toMutableList()
                    for(attendance in attendanceList.value){
                        var dateList = mutableListOf<String>()
                        dateList.addAll(attendance.date)
                        for(student in mappedStudentList!!){
                            if(student.id == attendance.id){
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

        val file = File("$path/attendance.csv")
        val writer = CSVWriter(FileWriter(file))
        val header = arrayOf("Id","Name","RollNo")
        header.plus(totalDates)
        Log.d("@@downloadCsv9", header.toString())
        writer.writeNext(header)
        for(item in attendanceList.value){
            val row = arrayOf(item.id,item.name,item.rollNumber)
            for(date in totalDates){
                var check = false
                for(dates in item.date ){
                    if(date == dates){
                        row.plus("1")
                        check = true
                        break
                    }
                }
                if(!check) {
                    row.plus("0")
                }
            }
            writer.writeNext(row)
        }
        writer.close()
        onSuccess()

    }
}