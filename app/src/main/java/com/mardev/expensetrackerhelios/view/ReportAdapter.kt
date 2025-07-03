package com.mardev.expensetrackerhelios.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mardev.expensetrackerhelios.R
import com.mardev.expensetrackerhelios.model.BudgetWithExpenses

class ReportAdapter: RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    private val list = mutableListOf<BudgetWithExpenses>()

    fun submitList(newList: List<BudgetWithExpenses>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ReportViewHolder(private val view: View):RecyclerView.ViewHolder(view){
        fun bind(data: BudgetWithExpenses){
            val tvBudgetName = view.findViewById<TextView>(R.id.tvBudgetName)
            val tvUsedBudget = view.findViewById<TextView>(R.id.tvUsedBudget)
            val tvMaxBudget = view.findViewById<TextView>(R.id.tvMaxBudget)
            val tvRemainingBudget = view.findViewById<TextView>(R.id.tvRemainingBudget)
            val progressBar = view.findViewById<ProgressBar>(R.id.progressBarBudget)

            tvBudgetName.text = data.budget.nama
            tvUsedBudget.text = "Terpakai: Rp ${data.totalExpense.toInt()}"
            tvMaxBudget.text = "Maksimum: Rp ${data.budget.nominal.toInt()}"
            tvRemainingBudget.text = "Sisa: Rp ${data.sisaBudget.toInt()}"
            progressBar.progress = data.progress
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}