package com.mardev.expensetrackerhelios.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BudgetingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg budget: Budgeting)

    @Query("SELECT * FROM budgeting")
    fun selectAllBudgets(): LiveData<List<Budgeting>>

    @Query("SELECT * FROM budgeting")
    suspend fun getAllBudgets(): List<Budgeting>

    @Query("SELECT * FROM budgeting WHERE id= :id")
    fun selectBudget(id: Int): Budgeting

    @Query("UPDATE budgeting SET Name_Budget=:name, Nominal_Budget=:nominal WHERE id = :id")
    fun update(name: String, nominal: Double, id: Int)

    @Update
    fun updateTodo(budget: Budgeting)

    @Delete
    fun deleteBudget(budget: Budgeting)
}
