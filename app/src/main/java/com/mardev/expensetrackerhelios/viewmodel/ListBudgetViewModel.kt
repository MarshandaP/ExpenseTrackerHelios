package com.mardev.expensetrackerhelios.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mardev.expensetrackerhelios.model.Budgeting
import com.mardev.expensetrackerhelios.model.ExpenseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListBudgetViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {

    val budgetLD = MutableLiveData<List<Budgeting>>()
    val budgetLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    init {
//        val db = ExpenseDatabase.getInstance(getApplication())
//        budgetLD = db.budgetingDao().selectAllBudgets()
        refresh()
    }

    fun clearTask(budget: Budgeting) {
        launch {
            val db = ExpenseDatabase.getInstance(getApplication())
            db.budgetingDao().deleteBudget(budget)
            refresh()
        }
    }

    fun refresh() {
        loadingLD.postValue(true)
        launch {
            val db = ExpenseDatabase.getInstance(getApplication())
//            val freshList = db.budgetingDao().getAllBudgets()
            val prefs = getApplication<Application>().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
            val username = prefs.getString("username", "") ?: ""
            val freshList = db.budgetingDao().getBudgetsByUser(username)
            (budgetLD as? MutableLiveData)?.postValue(freshList)
            loadingLD.postValue(false)
        }
    }
}