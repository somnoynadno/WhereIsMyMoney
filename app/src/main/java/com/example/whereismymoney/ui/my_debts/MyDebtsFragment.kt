package com.example.whereismymoney.ui.my_debts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.whereismymoney.R

class MyDebtsFragment : Fragment() {

    private lateinit var myDebtsViewModel: MyDebtsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myDebtsViewModel =
            ViewModelProviders.of(this).get(MyDebtsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_debts, container, false)
        val textView: TextView = root.findViewById(R.id.text_my_debts)
        myDebtsViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}