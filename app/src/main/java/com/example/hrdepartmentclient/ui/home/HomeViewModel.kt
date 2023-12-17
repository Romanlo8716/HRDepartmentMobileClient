package com.example.hrdepartmentclient.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Выберите с чем Вы хотите работать"
    }
    val text: LiveData<String> = _text
}