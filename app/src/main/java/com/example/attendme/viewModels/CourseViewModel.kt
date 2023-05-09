package com.example.attendme.viewModels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(val classID: String): ViewModel(){

}