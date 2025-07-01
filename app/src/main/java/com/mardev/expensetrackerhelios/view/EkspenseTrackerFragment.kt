package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.mardev.expensetrackerhelios.R
import com.mardev.expensetrackerhelios.databinding.FragmentBudgetingBinding
import com.mardev.expensetrackerhelios.databinding.FragmentEkspenseTrackerBinding

class EkspenseTrackerFragment : Fragment() {
    private lateinit var binding: FragmentEkspenseTrackerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEkspenseTrackerBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnExpense.setOnClickListener{
            val action = EkspenseTrackerFragmentDirections.actionNewExpense()
            Navigation.findNavController(it).navigate(action)
        }
    }

}