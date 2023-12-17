package com.example.hrdepartmentclient.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var bOpenWorkers: Button
    private lateinit var bOpenDepartments: Button
    private lateinit var bOpenPost: Button
    private lateinit var bOpenAdressOfDepartments: Button

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        bOpenWorkers = root.findViewById<Button>(R.id.bOpenWorkers)
        bOpenDepartments = root.findViewById<Button>(R.id.bOpenDepartments)
        bOpenPost = root.findViewById<Button>(R.id.bOpenPost)
        bOpenAdressOfDepartments = root.findViewById<Button>(R.id.bOpenAdressOfDepartments)



        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)



        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Нажатие на кнопку "Сотрудники"
        bOpenWorkers.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_workerFragment)
        }

        // Нажатие на кнопку "Отделы"
        bOpenDepartments.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_departmentFragment)
        }

        // Нажатие на кнопку "Должности"
        bOpenPost.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_postFragment)
        }

        // Нажатие на кнопку "Адреса отделов"
        bOpenAdressOfDepartments.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_adressOfDepartmentFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}