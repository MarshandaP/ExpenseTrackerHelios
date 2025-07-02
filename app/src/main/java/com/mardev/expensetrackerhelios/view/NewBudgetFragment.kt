package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mardev.expensetrackerhelios.R
import com.mardev.expensetrackerhelios.databinding.FragmentNewBudgetBinding
import com.mardev.expensetrackerhelios.model.Budgeting
import com.mardev.expensetrackerhelios.viewmodel.DetailBudgetViewModel

class NewBudgetFragment : Fragment() {
    private lateinit var binding: FragmentNewBudgetBinding
    private lateinit var viewModel:DetailBudgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewBudgetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this).get(DetailBudgetViewModel::class.java)

        binding.btnAddBudget.setOnClickListener{
            val name = binding.txtNameBudget.text.toString().trim()
            val nominalStr = binding.txtNominalBudget.text.toString().trim()

            if (binding.txtNameBudget.text.isNullOrBlank() || binding.txtNominalBudget.text.isNullOrBlank()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nominal = nominalStr.toDoubleOrNull()
            if (nominal == null || nominal <= 0.0) {
                Toast.makeText(requireContext(), "Nominal harus angka positif lebih dari 0!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var budget = Budgeting(
                binding.txtNameBudget.text.toString(),
                binding.txtNominalBudget.text.toString().toDoubleOrNull() ?: 0.0
            )

            val list = listOf(budget)
            viewModel.addBudget(list)
            Toast.makeText(view.context, "Data added", Toast.LENGTH_LONG).show()
            Navigation.findNavController(it).popBackStack()
        }
    }


}