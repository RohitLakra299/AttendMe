package com.example.attendme.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.attendme.model.Department
import com.example.attendme.model.ProfessorModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel@Inject constructor() : ViewModel(){
    val name = mutableStateOf("")
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val rePassword = mutableStateOf("")
    val department = mutableStateOf(Department.NONE)
    val status = mutableStateOf(false)
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore.collection("Professor")
    fun register(){
        if(name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.length >= 6 && rePassword.value == password.value){
            auth.createUserWithEmailAndPassword(email.value,password.value).addOnSuccessListener {
                val user = ProfessorModel(name.value,email.value, department.value)
                db.add(user).addOnSuccessListener {
                    status.value = true
                }
            }.addOnFailureListener {
                status.value = false
            }
        }

    }
}