package com.example.attendme.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel@Inject constructor() : ViewModel(){
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val status = mutableStateOf(false)
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    fun login() = CoroutineScope(Dispatchers.IO).launch{
        auth.signInWithEmailAndPassword(email.value,password.value).addOnSuccessListener {
            status.value = true
        }.addOnFailureListener {
            status.value = false
        }
    }
}