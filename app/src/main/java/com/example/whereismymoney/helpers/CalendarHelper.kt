package com.example.whereismymoney.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class CalendarHelper {
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateAsLong(): Long {
        val c = Calendar.getInstance()                // ЗАТО ОНО РАБОТАЕТ
        val year = c.get(Calendar.YEAR)              // ЗАТО ОНО РАБОТАЕТ
        val month = c.get(Calendar.MONTH)           // ЗАТО ОНО РАБОТАЕТ
        val day = c.get(Calendar.DAY_OF_MONTH)     // ЗАТО ОНО РАБОТАЕТ
                                                  // ЗАТО ОНО РАБОТАЕТ
        val chosenDate = day.toString() + "." +  // ЗАТО ОНО РАБОТАЕТ
                (month + 1).toString() + "." +  // ЗАТО ОНО РАБОТАЕТ
                year.toString()                // ЗАТО ОНО РАБОТАЕТ

        val pattern = "dd.MM.yyyy"

        return SimpleDateFormat(pattern).parse(chosenDate).time
    }

    fun getCurrentDateAsFormattedString(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        return sdf.format(Date())
    }
}