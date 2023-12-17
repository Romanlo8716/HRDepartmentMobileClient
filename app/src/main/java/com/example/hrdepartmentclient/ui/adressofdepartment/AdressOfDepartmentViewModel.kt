package com.example.hrdepartmentclient.ui.adressofdepartment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdressOfDepartmentViewModel :ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Адреса отделов"
    }
    val text: LiveData<String> = _text

}