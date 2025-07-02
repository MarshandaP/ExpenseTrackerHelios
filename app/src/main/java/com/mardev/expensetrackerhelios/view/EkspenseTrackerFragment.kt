package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardev.expensetrackerhelios.databinding.FragmentEkspenseTrackerBinding
import com.mardev.expensetrackerhelios.model.ExpenseDatabase
import com.mardev.expensetrackerhelios.model.ExpenseWithBudget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class EkspenseTrackerFragment : Fragment() {

    private lateinit var binding: FragmentEkspenseTrackerBinding
    private lateinit var adapter: ExpenseAdapterWithBudget

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEkspenseTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = ExpenseDatabase.getInstance(requireContext())

        adapter = ExpenseAdapterWithBudget { expenseWithBudget ->
            showExpenseDialog(expenseWithBudget)
        }

        binding.recExpense.layoutManager = LinearLayoutManager(requireContext())
        binding.recExpense.adapter = adapter

        lifecycleScope.launch {
            val expenses = withContext(Dispatchers.IO) {
                db.expenseDao().getAllExpensesWithBudget()
            }
            adapter.submitList(expenses)
        }

        binding.btnExpense.setOnClickListener {
            val action = EkspenseTrackerFragmentDirections.actionNewExpense()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun showExpenseDialog(data: ExpenseWithBudget) {
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val dateText = formatter.format(Date(data.expense.date))
        val message = """
            Tanggal     : $dateText
            Nominal     : Rp ${data.expense.amount.toInt()}
            Budget      : ${data.budget.nama}
            Keterangan  : ${data.expense.note}
        """.trimIndent()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Detail Pengeluaran")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
