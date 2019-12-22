package com.example.whereismymoney.models

import androidx.room.*

@Entity
data class Debt(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "debtor") val debtor: String,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "amount") val amount: Long,
    @ColumnInfo(name = "is_my_debt") val isMyDebt: Boolean,
    @ColumnInfo(name = "currency") val currency: String
)
