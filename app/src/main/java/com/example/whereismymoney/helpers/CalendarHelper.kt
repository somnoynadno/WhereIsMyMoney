package com.example.whereismymoney.helpers

import java.text.SimpleDateFormat
import java.util.*

class CalendarHelper {
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
        val date = SimpleDateFormat(pattern).parse(chosenDate).time

        return date
    }

    fun getCurrentDateAsFormatedString(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        return sdf.format(Date())
    }
}