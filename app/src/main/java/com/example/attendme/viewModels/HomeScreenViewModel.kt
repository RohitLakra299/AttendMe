package com.example.attendme.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
    private val db = Firebase.firestore.collection("Classes")
    private val auth = FirebaseAuth.getInstance()
    var classesList = mutableStateOf<List<ClassModel>>(emptyList())

    fun getClasses() = CoroutineScope(Dispatchers.IO).launch{
        val personQuery = db.whereEqualTo("id",auth.uid).get().await()
                if(personQuery.documents.isNotEmpty()){
                    val list = mutableListOf<ClassModel>()
                    for(doc in personQuery){
                        var classes = ClassModel(auth.uid!!,doc.get("className").toString(),doc.get("batch").toString(),doc.get("department").toString())
                        Log.d("@@getClasses",classes.toString())
                        list.add(classes)
                    }
                    classesList.value = list
                    Log.d("@@getClasses",classesList.toString())
                }

    }
}