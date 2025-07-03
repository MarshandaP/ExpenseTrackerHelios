package com.mardev.expensetrackerhelios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mardev.expensetrackerhelios.model.ExpenseDatabase
import com.mardev.expensetrackerhelios.model.ExpenseWithBudget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ExpenseTrackerViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    val expenseLD = MutableLiveData<List<ExpenseWithBudget>>()
    val loadingLD = MutableLiveData<Boolean>()
    val expenseLoadErrorLD = MutableLiveData<Boolean>()

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun refresh() {
        loadingLD.postValue(true)
        expenseLoadErrorLD.postValue(false)
        launch {
            try {
                val db = ExpenseDatabase.getInstance(getApplication())
                val prefs = getApplication<Application>().getSharedPreferences("user_pref", Application.MODE_PRIVATE)
                val username = prefs.getString("username", "") ?: ""

                val expenses = db.expenseDao().getExpensesWithBudgetByUser(username)
                expenseLD.postValue(expenses)
                loadingLD.postValue(false)
            } catch (e: Exception) {
                expenseLoadErrorLD.postValue(true)
                loadingLD.postValue(false)
            }
        }
    }
}
