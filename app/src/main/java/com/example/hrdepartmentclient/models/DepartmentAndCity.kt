package com.example.hrdepartmentclient.models

data class DepartmentAndCity(val city: String, val department: List<Department>, var isExpanded: Boolean = false) {

}