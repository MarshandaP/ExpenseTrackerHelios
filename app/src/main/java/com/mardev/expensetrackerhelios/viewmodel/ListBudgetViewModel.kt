package com.mardev.expensetrackerhelios.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

    fun refresh() {
        loadingLD.value = true
        budgetLoadErrorLD.value = false
        launch {
            val db = ExpenseDatabase.buildDatabase(
                getApplication()
            )
//            val db = buildDb(getApplication())

            budgetLD.postValue(db.budgetingDao().selectAllTodo())
            loadingLD.postValue(false)
        }
    }
}