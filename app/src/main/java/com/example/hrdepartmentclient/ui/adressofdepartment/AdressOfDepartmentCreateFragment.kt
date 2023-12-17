package com.example.hrdepartmentclient.ui.adressofdepartment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.repositories.AdressOfDepartmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdressOfDepartmentCreateFragment: Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_adressofdepartmentcreate, container, false)
        val adressOfDepartmentRepository = AdressOfDepartmentRepository()


        val editTextCity: EditText = view.findViewById(R.id.editTextCity)
        val editTextStreet: EditText = view.findViewById(R.id.editTextStreet)
        val editTextHouse: EditText = view.findViewById(R.id.editTextHouse)
        val addButton: Button = view.findViewById(R.id.createAdressOfDepartment)

        addButton.setOnClickListener {
            val city = editTextCity.text.toString()
            val street = editTextStreet.text.toString()
            val house = editTextHouse.text.toString()

            GlobalScope.launch(Dispatchers.Main) {
                val success = adressOfDepartmentRepository.createAdressOfDepartment(city,street,house)
                // Обработка результата (например, обновление UI)
                if (success) {
                    findNavController().navigate(R.id.action_nav_adressofdepartmentcreate_to_nav_adressOfDepartment)
                } else {

                }
            }
        }

        return view
    }
}