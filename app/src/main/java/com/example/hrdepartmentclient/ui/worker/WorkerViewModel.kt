package com.example.hrdepartmentclient.ui.worker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkerViewModel :ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Сотрудники"
    }
    val text: LiveData<String> = _text
}