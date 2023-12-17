package com.example.hrdepartmentclient.models

import java.util.Date

data class MedicalBook(val id: Int, val placeExam: String, val dateExam: Date, val conclusion: String, val worker: Worker ) {
}