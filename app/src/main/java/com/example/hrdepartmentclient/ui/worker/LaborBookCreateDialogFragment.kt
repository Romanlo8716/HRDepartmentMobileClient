package com.example.hrdepartmentclient.ui.worker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.hrdepartmentclient.R
import com.example.hrdepartmentclient.models.Worker
import java.util.Date

class LaborBookCreateDialogFragment : DialogFragment() {
    interface InputDialogListener {
        fun onInputDataAdded(dateOfLaborBook: Date, nameJob : String, information : String, nameDocument : String, numberDocument : String, worker : Worker)
    }

    private var listener: InputDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_laborbookcreate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datePickerDateOfLaborBook: DatePicker = view.findViewById(R.id.datePickerDateLaborBook)
        val editTextNameJob: EditText = view.findViewById(R.id.editTextNameJob)
        val editTextInform: EditText = view.findViewById(R.id.editTextInformation)
        val editTextNameDocument: EditText = view. findViewById(R.id.editTextNameDocument)
        val editTextNumberDocument: EditText = view. findViewById(R.id.editTextNumberDocument)
        val buttonAddInputData: Button = view.findViewById(R.id.buttonAddLaborBook)

//        buttonAddInputData.setOnClickListener {
//            val inputData = .text.toString().trim()
//
//            if (inputData.isNotEmpty()) {
//                listener?.onInputDataAdded(inputData)
//                dismiss()
//            } else {
//                // Показать сообщение об ошибке, если данные не введены
//                Toast.makeText(requireContext(), "Введите данные", Toast.LENGTH_SHORT).show()
//            }
//        }
    }

    fun setListener(listener: InputDialogListener) {
        this.listener = listener
    }
}