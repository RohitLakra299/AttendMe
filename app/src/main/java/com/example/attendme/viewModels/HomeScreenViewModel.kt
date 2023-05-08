package com.example.attendme.viewModels

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
class HomeScreenViewModel@Inject constructor() : ViewModel() {
    private val db = Firebase.firestore.collection("Professor")
    private val auth = FirebaseAuth.getInstance()
    val classesList = mutableListOf<ClassModel>()
    fun getClasses() = CoroutineScope(Dispatchers.IO).launch{
        val personQuery = db.whereEqualTo("id",auth.uid).get().await()
        if(personQuery.documents.isNotEmpty()){
            for (doc in personQuery){
                val dbClass = Firebase.firestore.collection("Professor/${doc.id}/Classes")
                val classQuery = dbClass.get().await()
                if(classQuery.documents.isNotEmpty()){
                    for(doc in classQuery){
                        var classes = ClassModel(auth.uid!!,doc.get("className").toString(),doc.get("batch").toString())
                        classesList.add(classes)
                    }
                }
            }
        }
    }
}