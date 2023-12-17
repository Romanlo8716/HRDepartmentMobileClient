package com.example.hrdepartmentclient.ui.department

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.Department
import com.example.hrdepartmentclient.models.DepartmentAndCity
import com.example.hrdepartmentclient.models.Post
import com.example.hrdepartmentclient.repositories.DepartmentRepository
import com.example.hrdepartmentclient.ui.post.PostFragmentDirections
import kotlinx.coroutines.launch


class DepartmentFragment : Fragment() {

    private lateinit var cityRecyclerView: RecyclerView
    private lateinit var departmentRecyclerView: RecyclerView
    private lateinit var departmentAdapter: DepartmentAdapter
    private val departmentRepository = DepartmentRepository()

    private val onCityClickListener: (DepartmentAndCity) -> Unit = { selectedCity ->
        // Обработка выбора города, например, загрузка данных отделов для выбранного города
        val departmentsForSelectedCity = selectedCity.department
        setupDepartmentRecyclerView(departmentsForSelectedCity)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_department, container, false)

        // Инициализация RecyclerView для городов
        cityRecyclerView = view.findViewById(R.id.cityRecyclerView)
        cityRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        cityRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // Инициализация RecyclerView для отделов
        departmentRecyclerView = view.findViewById(R.id.departmentRecyclerView)
        departmentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        departmentRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        // Инициализация адаптера
        departmentAdapter = DepartmentAdapter(emptyList(), { selectedCity ->
            // Обработка выбора города, например, загрузка данных отделов для выбранного города
            val departmentsForSelectedCity = selectedCity.department
            setupDepartmentRecyclerView(departmentsForSelectedCity)
        }) {
            // Обработка выбора отдела, например, вывод Toast с ID отдела

        }

        // Установка адаптера для городов
        cityRecyclerView.adapter = departmentAdapter

        // Загрузка данных и установка для городов
        lifecycleScope.launch {
            try {
                val cities = departmentRepository.getDepartment()
                setupCityRecyclerView(cities)
            } catch (e: Exception) {
                // Обработка ошибок, если необходимо
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonCreateDepartment = view?.findViewById<Button>(R.id.buttonCreateDepartment)

        buttonCreateDepartment?.setOnClickListener {
            findNavController().navigate(R.id.action_nav_department_to_nav_departmentcreate)
        }

    }

    private fun setupCityRecyclerView(cities: List<DepartmentAndCity>) {
        // Обновление данных в адаптере для городов
        departmentAdapter.updateData(cities)
    }

    private fun setupDepartmentRecyclerView(departments: List<Department>) {
        val onDepartmentClickListener: (Department) -> Unit = { selectedDepartment ->

            openDetailsFragment(selectedDepartment)

        }
        val departmentAdapter = DepartmentAdapter(departments, onCityClickListener, onDepartmentClickListener)
        departmentRecyclerView.adapter = departmentAdapter
    }

    private fun openDetailsFragment(selectedDepartment: Department) {
        val action = DepartmentFragmentDirections
            .actionNavDepartmentToNavDepartmentDetails(selectedDepartment.id.toString())
        findNavController().navigate(action)
    }

}