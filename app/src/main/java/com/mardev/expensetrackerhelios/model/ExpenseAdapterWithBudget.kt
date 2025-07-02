package com.mardev.expensetrackerhelios.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mardev.expensetrackerhelios.databinding.ItemExpenseBinding
import com.mardev.expensetrackerhelios.model.ExpenseWithBudget
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapterWithBudget(
    private val onClick: (ExpenseWithBudget) -> Unit
) : RecyclerView.Adapter<ExpenseAdapterWithBudget.ExpenseViewHolder>() {

    private val list = mutableListOf<ExpenseWithBudget>()

    fun submitList(newList: List<ExpenseWithBudget>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ExpenseViewHolder(val binding: ItemExpenseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ExpenseWithBudget) {
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
            binding.txtDate.text = formatter.format(Date(data.expense.date))
            binding.txtAmount.text = "Rp ${data.expense.amount.toInt()}"
            binding.chipBudget.text = data.budget.nama

            binding.txtAmount.setOnClickListener {
                onClick(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}
