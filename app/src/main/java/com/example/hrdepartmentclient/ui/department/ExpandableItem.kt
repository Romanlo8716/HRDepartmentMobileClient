package com.example.hrdepartmentclient.ui.department

import com.example.hrdepartmentclient.models.Department

sealed class ExpandableItem

data class CityItem(val city: String) : ExpandableItem()

data class DepartmentItem(val department: Department) : ExpandableItem()