package com.example.hrdepartmentclient.ui.department

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.hrdepartmentclient.models.AdressOfDepartment

class AddressAdapter(context: Context, resource: Int, objects: List<AdressOfDepartment>) :
    ArrayAdapter<AdressOfDepartment>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val address = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = "${address?.city}, ${address?.street}, ${address?.house}"

        return view
    }
}