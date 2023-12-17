package com.example.hrdepartmentclient.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.databinding.FragmentAdressofdepartmentBinding
import com.example.hrdepartmentclient.databinding.FragmentPostBinding
import com.example.hrdepartmentclient.models.AdressOfDepartment
import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.repositories.AdressOfDepartmentRepository
import com.example.hrdepartmentclient.repositories.PostRepository
import com.example.hrdepartmentclient.ui.adressofdepartment.AdressOfDepartmentAdapter
import com.example.hrdepartmentclient.ui.adressofdepartment.AdressOfDepartmentFragmentDirections
import com.example.hrdepartmentclient.ui.adressofdepartment.AdressOfDepartmentViewModel
import com.example.hrdepartmentclient.ui.adressofdepartment.OnItemClickListener
import com.example.hrdepartmentclient.ui.post.PostViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostFragment : Fragment(), com.example.hrdepartmentclient.ui.post.OnItemClickListener {

    private var _binding: FragmentPostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val repository = PostRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val galleryViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(PostViewModel::class.java)

        _binding = FragmentPostBinding.inflate(inflater, container, false)
        val root: View = binding.root



        val listView: ListView = binding.listView
        val adapter = PostAdapter(requireContext(), emptyList(), this) // Передаем пустой список при создании адаптера

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPost = adapter.getItem(position)
            openDetailsFragment(selectedPost)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val postList = withContext(Dispatchers.IO) {
                repository.getPost()
            }

            adapter.updateData(postList)
        }

        return root
    }

    private fun openDetailsFragment(selectedPost: Post) {
        val action = PostFragmentDirections
            .actionNavPostToNavPostDetails(selectedPost.id.toString())
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonCreatePost = view?.findViewById<Button>(R.id.buttonCreatePost)

        buttonCreatePost?.setOnClickListener {
            findNavController().navigate(R.id.action_nav_post_to_nav_postcreate)
        }

    }

    override fun onItemClick(post: Post) {
        openDetailsFragment(post)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}