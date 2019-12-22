package com.example.whereismymoney

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.appcompat.widget.Toolbar
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.new_debt_layout.*
import android.app.Activity
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.util.*
import java.text.SimpleDateFormat
import android.widget.CompoundButton
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class NewDebtActivity : AppCompatActivity() {

    private var chosenDate = ""
    private var date: Date? = null
    private var debtorName: String? = null
    private var isMyDebt: Boolean = false
    private val arraySpinner = arrayOf("RUB", "USD", "EUR", "NSD")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_debt_layout)

        val toolbar: Toolbar = findViewById(R.id.newDebtToolbar)
        setSupportActionBar(toolbar)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, arraySpinner
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        datePickerButton.setOnClickListener {
            alertDatePickerDialog()
        }

        saveButton.setOnClickListener {
            alertSaveDialog()
        }

        debtTypeSwitch.setOnCheckedChangeListener { _, _ ->
            isMyDebt = !isMyDebt
        }
    }


    private fun alertSaveDialog() {
        val builder = AlertDialog.Builder(this@NewDebtActivity)

        builder.setMessage("Сохранить?")

        builder.setPositiveButton("Да") { dialog, which ->
            saveDebtAndExit()
        }

        builder.setNeutralButton("Отмена") { _, _ ->
            Toast.makeText(
                applicationContext, "Отмена",
                Toast.LENGTH_SHORT
            ).show()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun alertDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                chosenDate = dayOfMonth.toString() + "." +
                        (monthOfYear + 1).toString() + "." +
                        year.toString()

                val toast = Toast.makeText(
                    applicationContext,
                    "Дата выбрана: " + chosenDate, Toast.LENGTH_SHORT
                )
                toast.show()

                val pattern = "dd.MM.yyyy"
                date = SimpleDateFormat(pattern).parse(chosenDate)
                println(date)

                dateText.text = "Вернёт до " + chosenDate
            },
            year,
            month,
            day
        )

        dpd.show()
    }


    private fun saveDebtAndExit() {
        // TODO: save debt
        val returnIntent = Intent()
        returnIntent.putExtra("result", 1)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
