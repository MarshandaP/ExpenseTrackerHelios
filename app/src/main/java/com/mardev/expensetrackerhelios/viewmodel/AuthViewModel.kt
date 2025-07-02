package com.mardev.expensetrackerhelios.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.expensetrackerhelios.model.ExpenseDatabase
import com.mardev.expensetrackerhelios.model.User
import kotlinx.coroutines.launch

class AuthViewModel(private val db: ExpenseDatabase) : ViewModel() {

    // Fungsi Sign In
    fun signIn(
        username: String,
        password: String,
        context: Context,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val user = db.userDao().getUserByUsername(username)
            if (user == null) {
                onResult(false, "Username tidak ditemukan!")
            } else if (user.password != password) {
                onResult(false, "Password salah!")
            } else {
                saveSession(context, username)
                onResult(true, "Login berhasil!")
            }
        }
    }

    // Fungsi Sign Up
    fun signUp(
        user: User,
        repeatPassword: String,
        context: Context,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            if (user.password != repeatPassword) {
                onResult(false, "Password dan Repeat Password tidak sama!")
                return@launch
            }
            if (db.userDao().checkUsernameExist(user.username) > 0) {
                onResult(false, "Username sudah digunakan!")
                return@launch
            }
            db.userDao().insertUser(user)
            saveSession(context, user.username)
            onResult(true, "Registrasi berhasil!")
        }
    }

    // Simpan sesi login (SharedPreferences)
    fun saveSession(context: Context, username: String) {
        val prefs = context.getSharedPreferences("session", 0)
        prefs.edit().putString("username", username).apply()
    }

    // Cek apakah user sudah login
    fun isLoggedIn(context: Context): Boolean {
        val prefs = context.getSharedPreferences("session", 0)
        return prefs.getString("username", null) != null
    }

    // Untuk logout
    fun logout(context: Context) {
        val prefs = context.getSharedPreferences("session", 0)
        prefs.edit().clear().apply()
    }
}
