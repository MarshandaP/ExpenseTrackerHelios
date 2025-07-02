package com.mardev.expensetrackerhelios.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Budgeting(
    @ColumnInfo(name = "Name_Budget")
    var nama: String,
    @ColumnInfo(name = "Nominal_Budget")
    var nominal: Double
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
