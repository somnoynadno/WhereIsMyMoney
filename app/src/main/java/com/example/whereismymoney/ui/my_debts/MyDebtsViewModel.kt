package com.example.whereismymoney.ui.my_debts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyDebtsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is my debts Fragment"
    }
    val text: LiveData<String> = _text
}