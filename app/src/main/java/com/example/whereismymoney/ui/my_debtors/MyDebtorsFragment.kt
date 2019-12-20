package com.example.whereismymoney.ui.my_debtors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.whereismymoney.R

class MyDebtorsFragment : Fragment() {

    private lateinit var myDebtorsViewModel: MyDebtorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myDebtorsViewModel =
            ViewModelProviders.of(this).get(MyDebtorsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_debtors, container, false)
        val textView: TextView = root.findViewById(R.id.text_my_debtors)
        myDebtorsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}