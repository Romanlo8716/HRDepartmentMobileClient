package com.example.hrdepartmentclient.ui.post

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.repositories.AdressOfDepartmentRepository
import com.example.hrdepartmentclient.repositories.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostCreateFragment: Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_postcreate, container, false)
        val postRepository = PostRepository()


        val editTextNamePost: EditText = view.findViewById(R.id.editTextNamePost)
        val editTextSalary: EditText = view.findViewById(R.id.editTextSalaty)

        val addButton: Button = view.findViewById(R.id.createPost)

        addButton.setOnClickListener {
            val namePost = editTextNamePost.text.toString()
            val salary = editTextSalary.text.toString()


            GlobalScope.launch(Dispatchers.Main) {
                val success = postRepository.createPost(namePost,salary)
                // Обработка результата (например, обновление UI)
                if (success) {
                    findNavController().navigate(R.id.action_nav_postcreate_to_nav_post)
                } else {

                }
            }
        }

        return view
    }
}