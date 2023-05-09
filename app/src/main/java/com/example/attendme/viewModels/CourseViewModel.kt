package com.example.attendme.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.attendme.model.ClassModel
import com.example.attendme.model.Department
import com.google.firebase.auth.FirebaseAuth
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
    private val auth = FirebaseAuth.getInstance()
    var currClass = mutableStateOf(ClassModel("",classID,"","",Department.NONE,0))
    init {
        getClassDetails()
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

}