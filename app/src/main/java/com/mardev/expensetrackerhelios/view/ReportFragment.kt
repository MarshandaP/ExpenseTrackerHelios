package com.mardev.expensetrackerhelios.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mardev.expensetrackerhelios.R
import com.mardev.expensetrackerhelios.databinding.FragmentReportBinding
import com.mardev.expensetrackerhelios.model.ExpenseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private lateinit var adapter: ReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = ExpenseDatabase.getInstance(requireContext())

        adapter = ReportAdapter()
        binding.recyclerViewReport.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewReport.adapter = adapter

        lifecycleScope.launch {
            val prefs = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
            val username = prefs.getString("username", "") ?: ""
            val budgetsWithExpenses = withContext(Dispatchers.IO) {
                db.budgetingDao().getBudgetsWithExpensesByUser(username)
            }

            adapter.submitList(budgetsWithExpenses)

            // Hitung total terpakai & total budget
            val totalExpense = budgetsWithExpenses.sumOf { it.totalExpense }
            val totalBudget = budgetsWithExpenses.sumOf { it.budget.nominal }
            binding.totalBudgetTextView.text = "Rp ${totalExpense.toInt()} / Rp ${totalBudget.toInt()}"
        }
    }
}