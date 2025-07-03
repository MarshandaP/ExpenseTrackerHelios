package com.mardev.expensetrackerhelios.model

import androidx.room.Embedded
import androidx.room.Relation

data class BudgetWithExpenses(
    @Embedded val budget: Budgeting,
    @Relation(
        parentColumn = "id",
        entityColumn = "budgetId"
    )
    val expense: List<Expense>
){
    val totalExpense: Double
        get() = expense.sumOf { it.amount }

    val sisaBudget: Double
        get() = budget.nominal - totalExpense

    val progress: Int
        get() = if(budget.nominal == 0.0) 0 else((totalExpense/budget.nominal) * 100).toInt()
}