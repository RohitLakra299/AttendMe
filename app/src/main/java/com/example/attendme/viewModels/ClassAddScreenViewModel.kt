package com.example.attendme.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.attendme.model.ClassModel
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
class ClassAddScreenViewModel@Inject constructor() : ViewModel(){
    val className = mutableStateOf("")
    val batch = mutableStateOf("")
    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore.collection("Professor")
    val status = mutableStateOf(false)
    fun createClass() = CoroutineScope(Dispatchers.IO).launch{
        val personQuery = db.whereEqualTo("id",auth.uid).get().await()
        var professorClass = ClassModel(auth.uid!!,className.value,batch.value)
        if(personQuery.documents.isNotEmpty()){
            for (doc in personQuery){
                val dbClass = Firebase.firestore.collection("Professor/${doc.id}/Classes")
                dbClass.add(professorClass).addOnSuccessListener {
                    status.value = true
                }.addOnFailureListener {
                    status.value = false
                }
            }
        }
    }
}