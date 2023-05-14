package com.example.attendme.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel@Inject constructor() : ViewModel(){
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    fun login(onSuccess:() -> Unit, onFailure: (String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO){
            if(email.value.isEmpty() || password.value.isEmpty())
            {
                withContext(Dispatchers.Main){
                    onFailure("Fill the required fields")
                }
                return@launch
            }
            auth.signInWithEmailAndPassword(email.value,password.value).addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                 onFailure(it.message)
            }
        }
    }
}