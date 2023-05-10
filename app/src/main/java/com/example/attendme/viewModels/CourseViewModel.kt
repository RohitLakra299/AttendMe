package com.example.attendme.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.attendme.model.ClassModel
import com.example.attendme.model.Department
import com.example.attendme.model.OtpModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(val classID: String): ViewModel(){
    private val db = Firebase.firestore.collection("Classes")
    private val studentDb = Firebase.firestore.collection("Students")
    private val otpDb = Firebase.firestore.collection("CurrentLiveAttendance")
    private val auth = FirebaseAuth.getInstance()
    var currClass = mutableStateOf(ClassModel("",classID,"","",Department.NONE,0))
    val otpValue = mutableStateOf("******")
    val isAttendance = mutableStateOf(false)
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
}