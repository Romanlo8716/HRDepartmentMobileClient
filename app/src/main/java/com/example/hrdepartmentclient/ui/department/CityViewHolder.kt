package com.example.hrdepartmentclient.ui.department

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.DepartmentAndCity

class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewCity: TextView = itemView.findViewById(R.id.textViewCity)

    fun bind(cityItem: DepartmentAndCity, onItemClick: (DepartmentAndCity) -> Unit) {
        textViewCity.text = cityItem.city

        itemView.setOnClickListener {
            onItemClick(cityItem)
        }
    }
}