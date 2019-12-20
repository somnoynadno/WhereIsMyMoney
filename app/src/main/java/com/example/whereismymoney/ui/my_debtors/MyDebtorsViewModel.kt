package com.example.whereismymoney.ui.my_debtors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyDebtorsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is debtors Fragment"
    }
    val text: LiveData<String> = _text
}