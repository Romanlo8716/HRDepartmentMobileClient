package com.example.hrdepartmentclient.ui.department

import com.example.hrdepartmentclient.models.Department
import com.example.hrdepartmentclient.models.Post

interface OnItemClickListener {
    fun onItemClick(department: Department)
}