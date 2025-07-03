package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mardev.expensetrackerhelios.R
import com.mardev.expensetrackerhelios.databinding.FragmentEditBudgetBinding
import com.mardev.expensetrackerhelios.databinding.FragmentNewBudgetBinding
import com.mardev.expensetrackerhelios.model.Budgeting
import com.mardev.expensetrackerhelios.viewmodel.DetailBudgetViewModel

class EditBudgetFragment : Fragment() {
    private lateinit var binding: FragmentEditBudgetBinding
    private lateinit var viewModel: DetailBudgetViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailBudgetViewModel::class.java)

        val uuid = EditBudgetFragmentArgs.fromBundle(requireArguments()).id

        viewModel.fetch(uuid)
        observeViewModel()

        binding.btnAddBudget.setOnClickListener {
            val nama = binding.txtNameBudget.text.toString()
            val nominal = binding.txtNominalBudget.text.toString().toDoubleOrNull() ?: 0.0

            val oldBudget = viewModel.budgetLD.value

            if (oldBudget != null) {
                val budget = Budgeting(nama, nominal, oldBudget.username).apply { id = uuid }
                viewModel.updateBudget(budget)
                Navigation.findNavController(it).popBackStack()
            } else {
                Toast.makeText(requireContext(), "Data lama tidak ditemukan!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.budgetLD.observe(viewLifecycleOwner) {
            binding.txtNameBudget.setText(it.nama)
            binding.txtNominalBudget.setText(it.nominal.toString())
        }
    }
}

