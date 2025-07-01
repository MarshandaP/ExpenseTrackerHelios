package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.mardev.expensetrackerhelios.R
import com.mardev.expensetrackerhelios.databinding.FragmentBudgetingBinding


class BudgetingFragment : Fragment() {
    private lateinit var binding: FragmentBudgetingBinding
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
        binding.btnBudget.setOnClickListener{
            val action = BudgetingFragmentDirections.actionNewBudget()
            Navigation.findNavController(it).navigate(action)
        }
    }

}