package com.example.hrdepartmentclient.ui.department

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hrdepartmentclient.R

class ExpandableRecyclerViewAdapter(private var items: List<ExpandableItem>, private val onItemClick: (ExpandableItem) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_CITY = 0
        private const val VIEW_TYPE_DEPARTMENT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_CITY -> {
                val view = inflater.inflate(R.layout.item_expandable, parent, false)
                ExpandableViewHolder(view)
            }
            VIEW_TYPE_DEPARTMENT -> {
                val view = inflater.inflate(R.layout.item_expandable, parent, false)
                ExpandableViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder) {
            is ExpandableViewHolder -> {
                holder.bind(item, onItemClick)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CityItem -> VIEW_TYPE_CITY
            is DepartmentItem -> VIEW_TYPE_DEPARTMENT
        }
    }

    fun updateData(newItems: List<ExpandableItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}

class ExpandableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewCity: TextView = itemView.findViewById(R.id.textViewCity)
    val textViewDepartment: TextView = itemView.findViewById(R.id.textViewDepartment)

    fun bind(item: ExpandableItem, onItemClick: (ExpandableItem) -> Unit) {
        when (item) {
            is CityItem -> {
                textViewCity.text = item.city
                textViewDepartment.visibility = View.GONE
            }
            is DepartmentItem -> {
                textViewCity.text = ""
                textViewDepartment.text = item.department.nameDepartment
                textViewDepartment.visibility = View.VISIBLE
            }

            else -> {}
        }

        itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}