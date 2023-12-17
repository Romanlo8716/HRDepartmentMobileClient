package com.example.hrdepartmentclient.models

import java.util.Date

data class Education(val id:Int, val seriesDiploma:String, val numberDiploma: String, val special: String, val dateYearEnd: Date, val worker: Worker) {
}