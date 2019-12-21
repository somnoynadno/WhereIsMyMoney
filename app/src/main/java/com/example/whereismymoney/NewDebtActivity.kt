package com.example.whereismymoney

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.appcompat.widget.Toolbar
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.new_debt_layout.*
import android.app.Activity


class NewDebtActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_debt_layout)

        val toolbar: Toolbar = findViewById(R.id.newDebtToolbar)
        setSupportActionBar(toolbar)

        val arraySpinner = arrayOf("RUB", "USD", "EUR", "NSD")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        saveButton.setOnClickListener{
            // save action
            val returnIntent = Intent()
            returnIntent.putExtra("result", 1)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}