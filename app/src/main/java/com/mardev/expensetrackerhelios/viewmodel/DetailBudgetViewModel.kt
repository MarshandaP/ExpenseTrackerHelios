package com.mardev.expensetrackerhelios.viewmodel

import android.app.Application
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

class DetailBudgetViewModel(application: Application)
    : AndroidViewModel(application), CoroutineScope {

    private val job = Job()
    val budgetLD = MutableLiveData<Budgeting>()


    fun addBudget(list:List<Budgeting>) {
        launch {
            val db = ExpenseDatabase.getInstance(
                getApplication()
            )
//            val db = buildDb(getApplication())
            db.budgetingDao().insertAll(*list.toTypedArray())
        }
    }

    fun fetch(uuid: Int) {
        launch {
            val db = ExpenseDatabase.getInstance(getApplication())
            val budget = db.budgetingDao().selectBudget(uuid)
            budgetLD.postValue(budget)
        }
    }

    fun updateBudget(budget: Budgeting) {
        launch {
            val db = ExpenseDatabase.getInstance(getApplication())
            db.budgetingDao().updateBudget(budget)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

}