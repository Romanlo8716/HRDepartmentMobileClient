package com.example.hrdepartmentclient.ui.adressofdepartment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.databinding.FragmentAdressofdepartmentBinding
import com.example.hrdepartmentclient.databinding.FragmentDepartmentBinding
import com.example.hrdepartmentclient.databinding.FragmentPostBinding
import com.example.hrdepartmentclient.models.AdressOfDepartment
import com.example.hrdepartmentclient.repositories.AdressOfDepartmentRepository
import com.example.hrdepartmentclient.repositories.PostRepository
import com.example.hrdepartmentclient.ui.department.DepartmentViewModel
import com.example.hrdepartmentclient.ui.post.PostAdapter
import com.example.hrdepartmentclient.ui.post.PostViewModel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdressOfDepartmentFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentAdressofdepartmentBinding? = null
    private val binding get() = _binding!!


    private val repository = AdressOfDepartmentRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdressofdepartmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView: ListView = binding.listView
        val adapter = AdressOfDepartmentAdapter(requireContext(), emptyList(), this)


        listView.adapter = adapter

        // Обработка кликов по элементам списка
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedAdress = adapter.getItem(position)
            openDetailsFragment(selectedAdress)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            val adressOfDepartmentList = withContext(Dispatchers.IO) {
                repository.getAdressOfDepartment()
            }

            adapter.updateData(adressOfDepartmentList)
        }

        return root
    }

    private fun openDetailsFragment(selectedAdress: AdressOfDepartment) {
        val action = AdressOfDepartmentFragmentDirections
            .actionNavAdressOfDepartmentToNavAdressOfDepartmentDetails(selectedAdress.id.toString())
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonCreateAdressOfDepartemnt = view?.findViewById<Button>(R.id.buttonCreateAdressOfDepartment)

        buttonCreateAdressOfDepartemnt?.setOnClickListener {
            findNavController().navigate(R.id.action_nav_adressOfDepartment_to_nav_adressofdepartmentcreate)
        }

    }

    override fun onItemClick(adressOfDepartment: AdressOfDepartment) {
        openDetailsFragment(adressOfDepartment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}