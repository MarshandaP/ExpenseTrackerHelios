package com.mardev.expensetrackerhelios.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Budgeting::class), version =  1)
abstract class ExpenseDatabase: RoomDatabase() {
    abstract fun budgetingDao(): BudgetingDao

    companion object {
        @Volatile private var instance: ExpenseDatabase ?= null
        private val LOCK = Any()

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ExpenseDatabase::class.java,
                "ExpenseDatabase").build()
                operator fun invoke(context:Context) {
                    if(instance == null) {
                        synchronized(LOCK) {
                            instance ?: buildDatabase(context).also {
                                instance = it
                            }
                        }
                    }
        }

    }

}
