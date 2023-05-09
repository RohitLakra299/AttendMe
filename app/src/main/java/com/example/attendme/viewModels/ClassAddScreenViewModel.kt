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
import javax.inject.Inject

@HiltViewModel
class ClassAddScreenViewModel @Inject constructor() : ViewModel() {
    val className = mutableStateOf("")
    val batch = mutableStateOf("")
    val department = mutableStateOf(Department.NONE)
    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore.collection("Classes")
    fun createClass(onSuccess: () -> Unit, onFailure: (String?) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            val classId = createClassId()
            var professorClass =
                ClassModel(
                    profId = auth.uid!!,
                    classId = classId,
                    className = className.value,
                    batch = batch.value,
                    department = department.value.toString()
                )
            db.add(professorClass).addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onFailure(it.message)
            }

        }
    private fun createClassId(): String {
        val alphanumeric = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..6).map { alphanumeric.random() }.joinToString("")
    }

}