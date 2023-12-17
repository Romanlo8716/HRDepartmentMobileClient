package com.example.hrdepartmentclient.ui.adressofdepartment

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
import com.example.hrdepartmentclient.repositories.AdressOfDepartmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdressOfDepartmentDetailsFragment : Fragment() {

    private var selectedAdressId: String? = null
    var repository = AdressOfDepartmentRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedAdressId?.let { id ->
            viewLifecycleOwner.lifecycleScope.launch {
                val adressDetails = repository.getAdressDetailsById(id)


                val buttonDelete = view.findViewById<Button>(R.id.bDeleteAdressOfDepartment)
                val buttonUpdate = view.findViewById<Button>(R.id.bEditAdressOfDepartment)

                buttonDelete.setOnClickListener{
                    GlobalScope.launch(Dispatchers.Main) {
                        val successDelete = repository.deleteAdressOfDepartment(id)
                        // Обработка результата (например, обновление UI)
                        if (successDelete) {
                            findNavController().navigate(R.id.action_nav_adressOfDepartmentDetails_to_nav_adressOfDepartment)
                        }
                    }
                }

                buttonUpdate.setOnClickListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val adressDetails = repository.getAdressDetailsById(id)
                        Log.d("AdressDetailsFragment", "Selected address details: $adressDetails")

                        openDetailsFragment(adressDetails)
                    }
                }


                // Теперь у вас есть данные о выбранном отделе
                // Используйте их для отображения на вашем макете
                // Например, если у вас есть TextView с id textViewDepartmentDetails:
                view.findViewById<TextView>(R.id.textViewCity)?.text = "город: ${adressDetails?.city}"
                view.findViewById<TextView>(R.id.textViewStreet)?.text = "улица: ${adressDetails?.street}"
                view.findViewById<TextView>(R.id.textViewHouse)?.text = "дом: ${adressDetails?.house}"
            }
        }
    }

    private fun openDetailsFragment(selectedAdress: AdressOfDepartment?) {
        val action = AdressOfDepartmentDetailsFragmentDirections.actionNavAdressOfDepartmentDetailsToNavAdressffdepartmentupdate(selectedAdress?.id.toString())
        findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            selectedAdressId = it.getString("selectedAdressId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_adressofdepaertmentdetails, container, false)




        return view
    }



}