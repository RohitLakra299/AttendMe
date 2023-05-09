package com.example.attendme.model

data class ClassModel(
    val profId : String,
    val classId: String,
    val className : String,
    val batch : String,
    val department : Department,
    val noOfStudents : Int
)