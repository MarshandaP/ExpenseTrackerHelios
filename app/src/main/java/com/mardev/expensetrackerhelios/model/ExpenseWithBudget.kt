package com.mardev.expensetrackerhelios.model

import androidx.room.Embedded
import androidx.room.Relation

data class ExpenseWithBudget(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "budgetId",
        entityColumn = "id"
    )
    val budget: Budgeting
)
