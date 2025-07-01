package com.mardev.expensetrackerhelios.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Budgeting(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "Name_Budget")
    val nama: String,
    @ColumnInfo(name = "Nominal_Budget")
    val nominal: Double
)
