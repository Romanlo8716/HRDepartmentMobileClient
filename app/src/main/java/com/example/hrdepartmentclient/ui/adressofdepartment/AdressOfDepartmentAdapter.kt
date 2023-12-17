package com.example.hrdepartmentclient.ui.adressofdepartment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast

import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.AdressOfDepartment
import androidx.fragment.app.FragmentManager
import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.ui.post.PostViewHolder

class AdressOfDepartmentAdapter(private val context: Context, private var adressOfDepartmentList: List<AdressOfDepartment>, private var listener : OnItemClickListener) : BaseAdapter() {

    override fun getCount(): Int {
        return adressOfDepartmentList.size
    }

    override fun getItem(position: Int): AdressOfDepartment {
        return adressOfDepartmentList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateData(newData: List<AdressOfDepartment>) {
        adressOfDepartmentList = newData
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: AdressOfDepartmentViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_adressofdepartment, parent, false)
            holder = AdressOfDepartmentViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as AdressOfDepartmentViewHolder
        }

        val adressOfDepartment = getItem(position)
        holder.textViewAddress.text = "${adressOfDepartment.city}, ${adressOfDepartment.street}, ${adressOfDepartment.house}"

        // Обработка нажатия на элемент списка
        view.setOnClickListener {
            listener?.onItemClick(adressOfDepartment)
        }

        return view
    }
}