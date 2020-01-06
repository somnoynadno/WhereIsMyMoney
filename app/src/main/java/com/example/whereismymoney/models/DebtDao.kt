package com.example.whereismymoney.models

import androidx.room.*

@Dao
interface DebtDao {
    @Query("SELECT * FROM debt ORDER BY date")
    fun getAll(): List<Debt>

    @Query("SELECT * FROM debt WHERE date = :d")
    fun getByDate(d: String): List<Debt>

    @Query("SELECT * FROM debt WHERE is_my_debt = 1 ORDER BY date")
    fun loadMyDebts(): List<Debt>

    @Query("SELECT * FROM debt WHERE is_my_debt = 0 ORDER BY date")
    fun loadNotMyDebts(): List<Debt>

    @Insert
    fun insertAll(vararg debts: Debt)

    @Delete
    fun delete(debt: Debt)
}