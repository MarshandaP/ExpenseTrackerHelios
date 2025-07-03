package com.mardev.expensetrackerhelios.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    suspend fun checkUsernameExist(username: String): Int

//    // ✅ Tambahan untuk ambil user by ID (untuk validasi password)
//    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
//    suspend fun selectUser(userId: Int): User
//
//    // ✅ Update password
//    @Query("UPDATE users SET password = :newPassword WHERE id = :userId")
//    suspend fun updatePassword(userId: Int, newPassword: String)
}
