package com.mardev.expensetrackerhelios.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardev.expensetrackerhelios.databinding.FragmentEkspenseTrackerBinding
import com.mardev.expensetrackerhelios.databinding.DialogExpenseDetailBinding
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

        val prefs = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = prefs.getString("username", "") ?: ""

        lifecycleScope.launch {
            val expenses = withContext(Dispatchers.IO) {
                db.expenseDao().getExpensesWithBudgetByUser(username)
            }
            adapter.submitList(expenses)
        }

        binding.btnExpense.setOnClickListener {
            val action = EkspenseTrackerFragmentDirections.actionNewExpense()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun showExpenseDialog(data: ExpenseWithBudget) {
        val dialogBinding = DialogExpenseDetailBinding.inflate(layoutInflater)

        val formatter = SimpleDateFormat("dd MMM yyyy HH.mm", Locale("id", "ID"))
        dialogBinding.txtDialogDate.text = formatter.format(Date(data.expense.date * 1000))
        dialogBinding.txtDialogNote.text = data.expense.note
        dialogBinding.txtDialogAmount.text = "IDR %,d".format(data.expense.amount.toInt())
        dialogBinding.chipDialogBudget.text = data.budget.nama

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnDialogClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
