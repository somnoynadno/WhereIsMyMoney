package com.example.whereismymoney.models

import androidx.room.*

@Database(entities = [Debt::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun debtDao(): DebtDao
}
