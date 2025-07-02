package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.mardev.expensetrackerhelios.databinding.FragmentNewExpenseBinding
import com.mardev.expensetrackerhelios.model.Budgeting
import com.mardev.expensetrackerhelios.model.Expense
import com.mardev.expensetrackerhelios.model.ExpenseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class NewExpenseFragment : Fragment() {

    private lateinit var binding: FragmentNewExpenseBinding
    private lateinit var budgetList: List<Budgeting>
    private var selectedBudget: Budgeting? = null
    private var totalUsed: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = ExpenseDatabase.getDatabase(requireContext())
        val today = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date())
        binding.txtDate.setText(today)

        // Load budgets into spinner
        lifecycleScope.launch {
            budgetList = withContext(Dispatchers.IO) {
                db.budgetingDao().getAllBudgets()
            }

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, budgetList.map { it.nama })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinKategori.adapter = adapter

            binding.spinKategori.setOnItemSelectedListener { _, _, position, _ ->
                selectedBudget = budgetList[position]
                updateProgressBar()
            }
        }

        binding.btnAddExpense.setOnClickListener {
            val nominalStr = binding.txtNominalExpense.text.toString()
            val notes = binding.txtNotesExpense.text.toString()

            if (nominalStr.isEmpty() || notes.isEmpty() || selectedBudget == null) {
                Toast.makeText(requireContext(), "Isi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nominal = nominalStr.toDoubleOrNull()
            if (nominal == null || nominal <= 0) {
                Toast.makeText(requireContext(), "Nominal tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val remaining = selectedBudget!!.nominal - totalUsed
            if (nominal > remaining) {
                Toast.makeText(requireContext(), "Pengeluaran melebihi budget!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expense = Expense(
                amount = nominal,
                note = notes,
                date = System.currentTimeMillis(),
                budgetId = selectedBudget!!.id
            )

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    db.expenseDao().insertExpense(expense)
                }
                Toast.makeText(requireContext(), "Berhasil menambahkan!", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(view).popBackStack()
            }
        }
    }

    private fun updateProgressBar() {
        val db = ExpenseDatabase.getDatabase(requireContext())

        lifecycleScope.launch {
            totalUsed = withContext(Dispatchers.IO) {
                db.expenseDao().getExpensesForBudgetNow(selectedBudget!!.id).sumOf { it.amount }
            }
            val remaining = (selectedBudget!!.nominal - totalUsed).coerceAtLeast(0.0)
            val progress = ((remaining / selectedBudget!!.nominal) * 100).toInt()
            binding.progressBarExpense.progress = progress
        }
    }
}
