package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardev.expensetrackerhelios.databinding.FragmentEkspenseTrackerBinding
import com.mardev.expensetrackerhelios.databinding.DialogExpenseDetailBinding
import com.mardev.expensetrackerhelios.model.ExpenseWithBudget
import com.mardev.expensetrackerhelios.viewmodel.ExpenseTrackerViewModel
import java.text.SimpleDateFormat
import java.util.*

class EkspenseTrackerFragment : Fragment() {

    private lateinit var binding: FragmentEkspenseTrackerBinding
    private lateinit var adapter: ExpenseAdapterWithBudget
    private lateinit var viewModel: ExpenseTrackerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEkspenseTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ExpenseTrackerViewModel::class.java)

        adapter = ExpenseAdapterWithBudget { expenseWithBudget ->
            showExpenseDialog(expenseWithBudget)
        }

        binding.recExpense.layoutManager = LinearLayoutManager(requireContext())
        binding.recExpense.adapter = adapter

        viewModel.refresh()
        observeViewModel()

        binding.btnExpense.setOnClickListener {
            val action = EkspenseTrackerFragmentDirections.actionNewExpense()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun observeViewModel() {
        viewModel.expenseLD.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.loadingLD.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading == true) {
                binding.recExpense.visibility = View.GONE
                binding.progressLoad.visibility = View.VISIBLE
            } else {
                binding.recExpense.visibility = View.VISIBLE
                binding.progressLoad.visibility = View.GONE
            }
        }

        viewModel.expenseLoadErrorLD.observe(viewLifecycleOwner) { isError ->
            if (isError == true) {
                // Tampilkan pesan error jika mau
            }
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
