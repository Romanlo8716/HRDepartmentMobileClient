package com.example.hrdepartmentclient.ui.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.AdressOfDepartment
import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.repositories.PostRepository
import com.example.hrdepartmentclient.ui.adressofdepartment.AdressOfDepartmentDetailsFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PostDetailsFragment : Fragment() {

    private var selectedPostId: String? = null
    var repository = PostRepository()

    companion object {
        private const val ARG_SELECTED_POST = "selected_post"

        fun newInstance(selectedPost: Post): PostDetailsFragment {
            val fragment = PostDetailsFragment()
            val args = Bundle()
            args.putParcelable(ARG_SELECTED_POST, selectedPost)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedPostId?.let { id ->
            viewLifecycleOwner.lifecycleScope.launch {
                val postDetails = repository.getPostDetailsById(id)

                // Теперь у вас есть данные о выбранном отделе
                // Используйте их для отображения на вашем макете
                // Например, если у вас есть TextView с id textViewDepartmentDetails:
                view.findViewById<TextView>(R.id.textViewNamePost)?.text = "Название должности: ${postDetails?.namePost}"
                view.findViewById<TextView>(R.id.textViewSalary)?.text = "Базовая ставка: ${postDetails?.salary}р."

                val buttonDelete = view.findViewById<Button>(R.id.bDeletePost)
                val buttonUpdate = view.findViewById<Button>(R.id.bEditPost)

                buttonDelete.setOnClickListener{
                    GlobalScope.launch(Dispatchers.Main) {
                        val successDelete = repository.deletePost(id)
                        // Обработка результата (например, обновление UI)
                        if (successDelete) {
                            findNavController().navigate(R.id.action_nav_postDetails_to_nav_post)
                        }
                    }
                }

                buttonUpdate.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val postDetails = repository.getPostDetailsById(id)


                        openDetailsFragment(postDetails)
                    }
                }

            }
        }
    }

    private fun openDetailsFragment(selectedPost: Post?) {
        val action = PostDetailsFragmentDirections.actionNavPostDetailsToNavPostupdate(selectedPost?.id.toString())
        findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            selectedPostId = it.getString("selectedPostId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_postdetails, container, false)
    }



}