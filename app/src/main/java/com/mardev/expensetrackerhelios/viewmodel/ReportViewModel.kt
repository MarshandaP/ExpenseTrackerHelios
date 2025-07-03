package com.mardev.expensetrackerhelios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mardev.expensetrackerhelios.model.BudgetWithExpenses
import com.mardev.expensetrackerhelios.model.ExpenseDatabase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ReportViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    val reportLD = MutableLiveData<List<BudgetWithExpenses>>()
    val loadingLD = MutableLiveData<Boolean>()
    val errorLD = MutableLiveData<Boolean>()

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun refresh() {
        loadingLD.postValue(true)
        launch {
            val db = ExpenseDatabase.getInstance(getApplication())
            val prefs = getApplication<Application>().getSharedPreferences("user_pref", 0)
            val username = prefs.getString("username", "") ?: ""

            // Ambil hanya budgets milik user ini
            val budgets = db.budgetingDao().getBudgetsWithExpenses().filter {
                it.budget.username == username
            }

            reportLD.postValue(budgets)
            loadingLD.postValue(false)
        }
    }
}
