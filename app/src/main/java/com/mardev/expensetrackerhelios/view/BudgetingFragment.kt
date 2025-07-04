package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mardev.expensetrackerhelios.R
import com.mardev.expensetrackerhelios.databinding.FragmentBudgetingBinding
import com.mardev.expensetrackerhelios.model.Budgeting
import com.mardev.expensetrackerhelios.viewmodel.ListBudgetViewModel


class BudgetingFragment : Fragment() {
    private lateinit var binding: FragmentBudgetingBinding
    private lateinit var viewModel: ListBudgetViewModel
    private lateinit var budgetListAdapter: BudgetListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBudgetingBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =ViewModelProvider(this).get(ListBudgetViewModel::class.java)

        budgetListAdapter = BudgetListAdapter(arrayListOf()) { budget ->
            val action = BudgetingFragmentDirections
                .actionEditBudget(budget.id)
            Navigation.findNavController(view).navigate(action)
        }

        viewModel.refresh()
        binding.recBudgeting.layoutManager = LinearLayoutManager(context)
        binding.recBudgeting.adapter = budgetListAdapter

        binding.btnBudget.setOnClickListener{
            val action = BudgetingFragmentDirections.actionNewBudget()
            Navigation.findNavController(it).navigate(action)
        }

        val navController = Navigation.findNavController(requireView())
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>("refresh")?.observe(
            viewLifecycleOwner
        ) {
            if (it == true) {
                viewModel.refresh()
            }
        }


        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.budgetLD.observe(viewLifecycleOwner, Observer {
            budgetListAdapter.updateBudgetlist(it)
            if(it.isEmpty()) {
                binding.recBudgeting?.visibility = View.GONE
            } else {
                binding.recBudgeting?.visibility = View.VISIBLE
            }
        })
        viewModel.loadingLD.observe(viewLifecycleOwner, Observer {
            if(it == false) {
                binding.progressLoad?.visibility = View.GONE
            } else {
                binding.progressLoad?.visibility = View.VISIBLE
            }
        })


    }

}