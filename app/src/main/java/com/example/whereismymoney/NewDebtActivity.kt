package com.example.whereismymoney

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.appcompat.widget.Toolbar
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.new_debt_layout.*
import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.title = getString(R.string.new_debt)
        toolbar.setNavigationOnClickListener { onBackPressed() }

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

        nameInput.hint = getString(R.string.name_input_hint_off)

        debtTypeSwitch.setOnCheckedChangeListener { _, _ ->
            isMyDebt = !isMyDebt
            if (isMyDebt) {
                switchText.text = getString(R.string.debt_type_switch_on)
                dateText.text = getString(R.string.date_text_on)
                nameInput.hint = getString(R.string.name_input_hint_on)
            } else {
                switchText.text = getString(R.string.debt_type_switch_off)
                dateText.text = getString(R.string.date_text_off)
                nameInput.hint = getString(R.string.name_input_hint_off)
            }
        }
    }

    // return true if error in validation
    private fun checkFields(): Boolean {
        var error = false

        if (nameInput.text!!.isEmpty()) {
            nameInputLayout.error = getString(R.string.name_layout_error)
            error = true
        } else nameInputLayout.error = null

        if (amountInput.text!!.isEmpty()) {
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

        builder.setMessage(getString(R.string.save_question))

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            saveDebtAndExit()
        }

        builder.setNegativeButton(getString(R.string.cancel)) { _, _ ->
            Toast.makeText(
                applicationContext, getString(R.string.cancel),
                Toast.LENGTH_SHORT
            ).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY)

    }


    @SuppressLint("SimpleDateFormat")
    private fun alertDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, thisYear, monthOfYear, dayOfMonth ->
                chosenDate = dayOfMonth.toString() + "." +
                        (monthOfYear + 1).toString() + "." +
                        thisYear.toString()

                val toast = Toast.makeText(
                    applicationContext,
                    "Дата выбрана: $chosenDate", Toast.LENGTH_SHORT
                )
                toast.show()

                val pattern = "dd.MM.yyyy"
                date = SimpleDateFormat(pattern).parse(chosenDate)
                println(date)

                if (isMyDebt)
                    dateText.text = getString(R.string.return_until_on, chosenDate)
                else
                    dateText.text = getString(R.string.return_until_off, chosenDate)
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
                currency = spinner.selectedItem.toString()
            )
        )

        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun onBackPressed() {
        if (nameInput.text.isNullOrEmpty() &&
            amountInput.text.isNullOrEmpty() &&
            chosenDate.isEmpty()
        ) {
            super.onBackPressed()
            return
        }

        val builder = AlertDialog.Builder(this@NewDebtActivity)

        builder.setMessage(getString(R.string.back_press_warning))

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            super.onBackPressed()
        }

        builder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY)
    }
}
