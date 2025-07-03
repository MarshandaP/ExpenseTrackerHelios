package com.mardev.expensetrackerhelios.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mardev.expensetrackerhelios.databinding.BudgetListItemBinding
import com.mardev.expensetrackerhelios.model.Budgeting

class BudgetListAdapter(val budgetList: ArrayList<Budgeting>,val onClick: (Budgeting) -> Unit) :
    RecyclerView.Adapter<BudgetListAdapter.BudgetViewHolder>() {

        class BudgetViewHolder(val binding: BudgetListItemBinding)
            : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
            var binding = BudgetListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent,false)
            return BudgetViewHolder(binding)
        }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val budget = budgetList[position]
        holder.binding.txtNamaBud2.text = budget.nama
        holder.binding.txtNomBud2.text = budget.nominal.toString()
        holder.binding.root.setOnClickListener { onClick(budget)
        }
    }

    override fun getItemCount(): Int {
        return budgetList.size
    }

    fun updateBudgetlist(editBudgetList: List<Budgeting>){
        budgetList.clear()
        budgetList.addAll(editBudgetList)
        notifyDataSetChanged()
    }
}