package com.example.hrdepartmentclient.ui.post

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.repositories.AdressOfDepartmentRepository
import com.example.hrdepartmentclient.repositories.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostUpdateFragment : Fragment() {

    private var selectedPostId: String? = null
    var repository = PostRepository()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_postupdate, container, false)


        selectedPostId?.let { id ->
            viewLifecycleOwner.lifecycleScope.launch {
                val postDetails = repository.getPostDetailsById(id)

                val editTextNamePost: EditText = view.findViewById(R.id.editTextNamePostUpdate)
                val editTextSalary: EditText = view.findViewById(R.id.editTextSalaryUpdate)

                val updateButton: Button = view.findViewById(R.id.updatePost)

                if (postDetails != null) {
                    editTextNamePost.setText(postDetails.namePost)
                    editTextSalary.setText(postDetails.salary)

                }

                updateButton.setOnClickListener {
                    val namePost = editTextNamePost.text.toString()
                    val salary = editTextSalary.text.toString()


                    GlobalScope.launch(Dispatchers.Main) {
                        val success = repository.updatePost(postDetails?.id.toString(),namePost, salary)
                        // Обработка результата (например, обновление UI)
                        if (success) {
                            findNavController().navigate(R.id.action_nav_postupdate_to_nav_post)
                        } else {

                        }
                    }
                }

            }
        }


        return view
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            selectedPostId = it.getString("selectedPostId")
        }
    }
}