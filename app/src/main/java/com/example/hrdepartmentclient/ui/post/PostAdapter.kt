package com.example.hrdepartmentclient.ui.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.AdressOfDepartment
import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.ui.adressofdepartment.AdressOfDepartmentViewHolder
import com.example.hrdepartmentclient.ui.adressofdepartment.OnItemClickListener

class PostAdapter(private val context: Context, private var postList: List<Post>, private var listener : com.example.hrdepartmentclient.ui.post.OnItemClickListener) : BaseAdapter() {

    override fun getCount(): Int {
        return postList.size
    }

    override fun getItem(position: Int): Post {
        return postList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateData(newData: List<Post>) {
        postList = newData
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: PostViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
            holder = PostViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as PostViewHolder
        }

        val post = getItem(position)
        holder.textViewPost.text = "${post.namePost} \n ${post.salary}р."

        // Обработка нажатия на элемент списка
        view.setOnClickListener {
            listener?.onItemClick(post)
        }

        return view
    }
}