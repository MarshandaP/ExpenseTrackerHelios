package com.mardev.expensetrackerhelios.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mardev.expensetrackerhelios.databinding.BudgetListItemBinding
import com.mardev.expensetrackerhelios.model.Budgeting

class BudgetListAdapter(val budgetList: ArrayList<Budgeting>) :
    RecyclerView.Adapter<BudgetListAdapter.BudgetViewHolder>() {

        class BudgetViewHolder(val binding: BudgetListItemBinding)
            : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
            var binding = BudgetListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent,false)
            return BudgetViewHolder(binding)
        }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.binding.budget = budgetList[position]
    }

    override fun getItemCount(): Int {
        return budgetList.size
    }

    fun updateBudgetlist(newBudgetList: List<Budgeting>){
        budgetList.clear()
        budgetList.addAll(newBudgetList)
        notifyDataSetChanged()
    }
}