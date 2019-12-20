package com.example.whereismymoney.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TodayViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is my today Fragment"
    }
    val text: LiveData<String> = _text
}