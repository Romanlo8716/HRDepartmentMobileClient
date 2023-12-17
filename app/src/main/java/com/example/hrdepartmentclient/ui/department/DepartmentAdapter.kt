package com.example.hrdepartmentclient.ui.department

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hrdepartmentclient.models.Department
import com.example.hrdepartmentclient.models.DepartmentAndCity


class DepartmentAdapter(
    private var items: List<Any>, // Теперь адаптер может работать с городами и отделами
    private val onCityClickListener: (DepartmentAndCity) -> Unit,
    private val onDepartmentClickListener: (Department) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CITY = 0
        private const val VIEW_TYPE_DEPARTMENT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View

        return when (viewType) {
            VIEW_TYPE_CITY -> {
                view = LayoutInflater.from(parent.context).inflate(com.example.hrdepartmentclient.R.layout.item_city, parent, false)

                CityViewHolder(view)
            }
            VIEW_TYPE_DEPARTMENT -> {
                view = LayoutInflater.from(parent.context).inflate(com.example.hrdepartmentclient.R.layout.item_department, parent, false)
                DepartmentViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    fun updateData(newData: List<DepartmentAndCity>) {
        items = newData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder) {
            is CityViewHolder -> {
                val cityItem = item as DepartmentAndCity
                holder.bind(cityItem, onCityClickListener)
            }
            is DepartmentViewHolder -> {
                val departmentItem = item as Department
                holder.bind(departmentItem, onDepartmentClickListener)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is DepartmentAndCity) {
            VIEW_TYPE_CITY
        } else {
            VIEW_TYPE_DEPARTMENT
        }
    }

    fun setCitiesAndDepartments(citiesAndDepartments: List<DepartmentAndCity>) {
        items = citiesAndDepartments
        notifyDataSetChanged()
    }
    // ViewHolder classes go here
}