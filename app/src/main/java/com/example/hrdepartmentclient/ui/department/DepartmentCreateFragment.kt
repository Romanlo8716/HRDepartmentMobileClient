package com.example.hrdepartmentclient.ui.department

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.AdressOfDepartment
import com.example.hrdepartmentclient.repositories.AdressOfDepartmentRepository
import com.example.hrdepartmentclient.repositories.DepartmentRepository
import com.example.hrdepartmentclient.repositories.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DepartmentCreateFragment: Fragment() {

    private var idAdress = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_department_create, container, false)
        val departmentRepository = DepartmentRepository()
        val adressOfDepartmentRepository = AdressOfDepartmentRepository()


        val editTextNameDepartment: EditText = view.findViewById(R.id.editTextNameDepartment)
        val editTextPhone: EditText = view.findViewById(R.id.editTextPhone)

        val spinner = view.findViewById<Spinner>(R.id.departmentSpinner)

// Пример данных
        viewLifecycleOwner.lifecycleScope.launch {
            val addresses = adressOfDepartmentRepository.getAdressOfDepartment()

            val adapter = AddressAdapter(requireContext(), android.R.layout.simple_spinner_item, addresses)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                // Получаем выбранный элемент из Spinner
                val selectedAddress = spinner.selectedItem as AdressOfDepartment?
                // Теперь у вас есть доступ к данным выбранного адреса
                if (selectedAddress != null) {
                    val idAdressOfDepartment = selectedAddress.id
                    idAdress = idAdressOfDepartment
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Обработка события, когда ничего не выбрано
            }
        }


        val addButton: Button = view.findViewById(R.id.createDepartment)

        addButton.setOnClickListener {
            val nameDepartment = editTextNameDepartment.text.toString()
            val phone = editTextPhone.text.toString()

            GlobalScope.launch(Dispatchers.Main) {
                val success = departmentRepository.createDepartment(nameDepartment,phone, idAdress)
                // Обработка результата (например, обновление UI)
                if (success) {
                    findNavController().navigate(R.id.action_nav_departmentcreate_to_nav_department)
                } else {

                }
            }
        }

        return view
    }
}