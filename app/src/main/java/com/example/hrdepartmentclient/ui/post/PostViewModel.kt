package com.example.hrdepartmentclient.ui.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Должности"
    }
    val text: LiveData<String> = _text
}