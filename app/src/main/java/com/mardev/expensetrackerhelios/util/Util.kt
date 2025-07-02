package com.mardev.expensetrackerhelios.util

import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mardev.expensetrackerhelios.model.ExpenseDatabase

val DB_NAME = "ExpenseDatabase"

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE todo ADD COLUMN priority INTEGER DEFAULT 3 not null")
    }
}

fun buildDb(context: Context): ExpenseDatabase {
    val db = ExpenseDatabase.buildDatabase(context)
    return db
}