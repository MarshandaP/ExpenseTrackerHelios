package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mardev.expensetrackerhelios.databinding.FragmentReportBinding
import com.mardev.expensetrackerhelios.viewmodel.ReportViewModel

class ReportFragment : Fragment() {

    private lateinit var binding: FragmentReportBinding
    private lateinit var adapter: ReportAdapter
    private lateinit var viewModel: ReportViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ReportAdapter()
        binding.recyclerViewReport.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewReport.adapter = adapter

        viewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
        viewModel.refresh()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.reportLD.observe(viewLifecycleOwner, Observer { budgetsWithExpenses ->
            budgetsWithExpenses?.let {
                adapter.submitList(it)
                // Hitung total
                val totalExpense = it.sumOf { item -> item.totalExpense }
                val totalBudget = it.sumOf { item -> item.budget.nominal }
                binding.totalBudgetTextView.text = "Rp ${totalExpense.toInt()} / Rp ${totalBudget.toInt()}"
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.recyclerViewReport.visibility = if (isLoading) View.GONE else View.VISIBLE
            binding.totalBudgetTextView.visibility = if (isLoading) View.GONE else View.VISIBLE
        })

        viewModel.errorLD.observe(viewLifecycleOwner, Observer { isError ->
            // Kalau mau, bisa munculkan error UI
        })
    }
}
