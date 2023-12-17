package com.example.hrdepartmentclient.ui.department

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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

class DepartmentUpdateFragment: Fragment() {

    private var selectedDepartmentId: String? = null
    var repository = DepartmentRepository()
    var adressOfDepartmentRepository = AdressOfDepartmentRepository()
    private var idAdress = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_departmentupdate, container, false)


        selectedDepartmentId?.let { id ->
            viewLifecycleOwner.lifecycleScope.launch {
                val departmentDetails = repository.getDepartmentDetailsById(id)

                val updateButton: Button = view.findViewById(R.id.updateDepartment)
                val editTextNameDepartment: EditText = view.findViewById(R.id.editTextNameDepartmentUpdate)
                val editTextPhone: EditText = view.findViewById(R.id.editTextPhoneUpdate)
                val spinnerAdressOfDepartment : Spinner = view.findViewById(R.id.departmentSpinnerUpdate)



                viewLifecycleOwner.lifecycleScope.launch {
                    val addresses = adressOfDepartmentRepository.getAdressOfDepartment()

                    val adapter = AddressAdapter(requireContext(), android.R.layout.simple_spinner_item, addresses)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerAdressOfDepartment.adapter = adapter

                    if (addresses.isNotEmpty()) {
                        // Предположим, что у вас есть начальный объект AdressOfDepartment, который вы хотите выбрать
                        val initialAddress: AdressOfDepartment = departmentDetails!!.adressOfDepartment// ваш объект AdressOfDepartment
                        val initialPosition = addresses.indexOf(initialAddress)
                        spinnerAdressOfDepartment.setSelection(initialPosition)
                    }
                }

                spinnerAdressOfDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                        // Получаем выбранный элемент из Spinner
                        val selectedAddress = spinnerAdressOfDepartment.selectedItem as AdressOfDepartment?
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


                if (departmentDetails != null) {
                    editTextNameDepartment.setText(departmentDetails.nameDepartment)
                    editTextPhone.setText(departmentDetails.phoneNumber)
                    //spinnerAdressOfDepartment.set

                }

                updateButton.setOnClickListener {
                    val nameDepartment = editTextNameDepartment.text.toString()
                    val phone = editTextPhone.text.toString()


                    GlobalScope.launch(Dispatchers.Main) {
                        val success = repository.updateDepartment(departmentDetails?.id.toString(),nameDepartment, phone, idAdress)
                        // Обработка результата (например, обновление UI)
                        if (success) {
                            findNavController().navigate(R.id.action_nav_departmentupdate_to_nav_department)
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
            selectedDepartmentId = it.getString("selectedDepartmentId")
        }
    }
}