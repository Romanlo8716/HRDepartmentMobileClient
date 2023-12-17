package com.example.hrdepartmentclient.models

import java.util.Date

data class Vacation(val id:Int, val typeVacation:String, val dateStartVacation:Date, val dateEndVacation:Date, val footing:String, val worker:Worker) {
}