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
import androidx.room.Room
import com.example.whereismymoney.models.AppDatabase
import com.example.whereismymoney.models.Debt
import java.util.*
import java.text.SimpleDateFormat


class NewDebtActivity : AppCompatActivity() {

    private var chosenDate = ""
    private var date: Date? = null
    private var isMyDebt: Boolean = false
    private val arraySpinner = arrayOf("RUB", "USD", "EUR")

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
            if (!checkFields()) alertSaveDialog()
        }

        debtTypeSwitch.setOnCheckedChangeListener { _, _ ->
            isMyDebt = !isMyDebt
        }
    }

    // return true if error in validation
    private fun checkFields(): Boolean {
        var error = false

        if (nameInput.text!!.length.equals(0)) {
            nameInputLayout.error = getString(R.string.name_layout_error)
            error = true
        } else nameInputLayout.error = null

        if (amountInput.text!!.length.equals(0)) {
            amountInputLayout.error = getString(R.string.amount_layout_error)
            error = true
        } else amountInputLayout.error = null

        if (date == null) {
            dateInputLayout.error = getString(R.string.date_layout_error)
            error = true
        } else dateInputLayout.error = null

        return error
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
        val debtor = nameInput.text.toString()              //  они не пустые
        val amount = amountInput.text.toString().toLong()   // я это гарантирую

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "debts"
        ).allowMainThreadQueries().build()

        db.debtDao().insertAll(
            Debt(
                debtor = debtor,
                amount = amount,
                date = date!!.time,
                isMyDebt = isMyDebt,
                currency = spinner.getSelectedItem().toString()
            )
        )

        val returnIntent = Intent()
        returnIntent.putExtra("result", 1)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
