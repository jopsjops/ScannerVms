package com.example.scanner.model

data class ViolationData(
    val studentId: String,
    val studentName: String,
    val department: String,
    val program: String,
    val offense: String?,
    val violation: String,
    val personnelName: String,
    val email: String,
    val date: String,
    val time: String
)
