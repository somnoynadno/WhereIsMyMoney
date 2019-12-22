package com.example.whereismymoney.models

import androidx.room.*

@Dao
interface DebtDao {
    @Query("SELECT * FROM debt")
    fun getAll(): List<Debt>

    @Query("SELECT * FROM debt WHERE is_my_debt = 1")
    fun loadMyDebts(): List<Debt>

    @Query("SELECT * FROM debt WHERE is_my_debt = 0")
    fun loadNotMyDebts(): List<Debt>

    @Insert
    fun insertAll(vararg debts: Debt)

    @Delete
    fun delete(debt: Debt)
}