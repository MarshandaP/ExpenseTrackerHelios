package com.mardev.expensetrackerhelios.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val firstName: String,
    val lastName: String,
    val password: String
)
