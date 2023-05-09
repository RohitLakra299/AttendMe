package com.example.attendme.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.attendme.model.ClassModel
import com.example.attendme.model.Department
import com.example.attendme.model.ProfessorModel
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
class HomeScreenViewModel @Inject constructor() : ViewModel() {
    private val db = Firebase.firestore.collection("Classes")
    private val professorDb = Firebase.firestore.collection("Professor")
    private val auth = FirebaseAuth.getInstance()
    var classesList = mutableStateOf<List<ClassModel>>(emptyList())
    var professor = mutableStateOf<ProfessorModel>(ProfessorModel("","","",Department.NONE))
    init {
       getProfessor()
    }
    fun getClasses() = CoroutineScope(Dispatchers.IO).launch {
        val personQuery = db.whereEqualTo("profId", auth.uid).get().await()
        if (personQuery.documents.isNotEmpty()) {
            val list = mutableListOf<ClassModel>()
            for (doc in personQuery) {
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
                var classes = ClassModel(
                    auth.uid!!,
                    doc.get("classId").toString(),
                    doc.get("className").toString(),
                    doc.get("batch").toString(),
                    department,
                    doc.get("noOfStudents").toString().toInt()
                )
                Log.d("@@getClasses", classes.toString())
                list.add(classes)
            }
            classesList.value = list
            Log.d("@@getClasses", classesList.toString())
        }

    }
    fun getProfessor() = CoroutineScope(Dispatchers.IO).launch {
        val professorQuery = professorDb.whereEqualTo("id",auth.uid).get().await()
        if(professorQuery.documents.isNotEmpty()){
            for (doc in professorQuery){
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
                var prof = ProfessorModel(auth.uid!!,doc.get("name").toString(),doc.get("email").toString(),department)
                professor.value = prof
            }
        }
    }
}