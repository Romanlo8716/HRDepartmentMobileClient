package com.example.hrdepartmentclient.ui.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DepartmentViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Отделы"
    }
    val text: LiveData<String> = _text
}