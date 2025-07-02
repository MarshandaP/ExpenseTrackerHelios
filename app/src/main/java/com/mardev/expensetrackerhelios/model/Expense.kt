package com.mardev.expensetrackerhelios.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Expense")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val note: String,
    val date: Long,
    val budgetId: Int
)
