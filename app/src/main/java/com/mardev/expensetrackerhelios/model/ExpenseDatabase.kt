package com.mardev.expensetrackerhelios.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Budgeting::class], version = 1)
abstract class ExpenseDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun budgetingDao(): BudgetingDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var instance: ExpenseDatabase? = null

        fun getInstance(context: Context): ExpenseDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ExpenseDatabase::class.java,
                "ExpenseDatabase"
            ).build()
    }
}
