package com.example.hrdepartmentclient.ui.adressofdepartment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.example.hrdepartmentclient.models.AdressOfDepartment
import com.example.hrdepartmentclient.repositories.AdressOfDepartmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdressOfDepartmentUpdateFragment: Fragment() {

    private var selectedAdressId: String? = null
    var repository = AdressOfDepartmentRepository()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_adressofdepartmentupdate, container, false)


        selectedAdressId?.let { id ->
            viewLifecycleOwner.lifecycleScope.launch {
                val adressDetails = repository.getAdressDetailsById(id)

                val editTextCity: EditText = view.findViewById(R.id.editTextCityUpdate)
                val editTextStreet: EditText = view.findViewById(R.id.editTextStreetUpdate)
                val editTextHouse: EditText = view.findViewById(R.id.editTextHouseUpdate)
                val updateButton: Button = view.findViewById(R.id.updateAdressOfDepartment)

                if (adressDetails != null) {
                    editTextCity.setText(adressDetails.city)
                    editTextStreet.setText(adressDetails.street)
                    editTextHouse.setText(adressDetails.house)
                }

                updateButton.setOnClickListener {
                    val city = editTextCity.text.toString()
                    val street = editTextStreet.text.toString()
                    val house = editTextHouse.text.toString()

                    GlobalScope.launch(Dispatchers.Main) {
                        val success = repository.updateAdressOfDepartment(adressDetails?.id.toString(),city,street,house)
                        // Обработка результата (например, обновление UI)
                        if (success) {
                            findNavController().navigate(R.id.action_nav_adressofdepartmentupdate_to_nav_adressOfDepartment)
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
            selectedAdressId = it.getString("selectedAdressId")
        }
    }
}