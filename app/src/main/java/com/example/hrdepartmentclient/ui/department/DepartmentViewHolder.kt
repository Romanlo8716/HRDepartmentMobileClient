package com.example.hrdepartmentclient.ui.department

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.Department
import com.example.hrdepartmentclient.models.DepartmentAndCity

class DepartmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewDepartment: TextView = itemView.findViewById(R.id.textViewDepartment)
    val textViewPhoneNumber: TextView = itemView.findViewById(R.id.textViewPhoneNumber)
    // Добавьте другие представления, если необходимо

    fun bind(department: Department, onItemClick: (Department) -> Unit) {
        textViewDepartment.text = department.nameDepartment
        textViewPhoneNumber.text = department.phoneNumber
        // Другие привязки данных

        itemView.setOnClickListener {
            onItemClick.invoke(department)
        }
    }
}