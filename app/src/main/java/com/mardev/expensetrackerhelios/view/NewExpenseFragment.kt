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

    private var _binding: FragmentNewExpenseBinding? = null
    private val binding get() = _binding!!
    private lateinit var budgetList: List<Budgeting>
    private var selectedBudget: Budgeting? = null
    private var totalUsed: Double = 0.0
    private var selectedDate: Long = System.currentTimeMillis() / 1000


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = ExpenseDatabase.getInstance(requireContext())
        val today = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")).format(Date())
        binding.txtDate.setText(today)
        binding.txtDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dialog = android.app.DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = calendar.timeInMillis / 1000
                    val formatted = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                        .format(Date(calendar.timeInMillis))
                    binding.txtDate.setText(formatted)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }

        lifecycleScope.launch {
            budgetList = withContext(Dispatchers.IO) {
                db.budgetingDao().getAllBudgets()
            }

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                budgetList.map { it.nama }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinKategori.adapter = adapter

            binding.spinKategori.onItemSelectedListener = object :
                android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: android.widget.AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedBudget = budgetList[position]
                    updateProgressBar()
                }

                override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
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
                date = selectedDate,
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
        val db = ExpenseDatabase.getInstance(requireContext())

        lifecycleScope.launch {
            totalUsed = withContext(Dispatchers.IO) {
                db.expenseDao().getExpensesForBudgetNow(selectedBudget!!.id).sumOf { it.amount }
            }
            val remaining = (selectedBudget!!.nominal - totalUsed).coerceAtLeast(0.0)
            val progress = ((remaining / selectedBudget!!.nominal) * 100).toInt()
            binding.progressBarExpense.progress = progress
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
