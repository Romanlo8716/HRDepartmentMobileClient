package com.example.hrdepartmentclient.models

import java.util.Date

data class LaborBook(val id:Int, val dateRecord: Date, val nameWork: String, val informationAboutWork: String, val nameDocument: String, val numberDocument: String, val worker: Worker) {
}