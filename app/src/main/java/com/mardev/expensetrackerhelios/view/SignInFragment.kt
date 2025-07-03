package com.mardev.expensetrackerhelios.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mardev.expensetrackerhelios.R
import com.mardev.expensetrackerhelios.databinding.FragmentSignInBinding
import com.mardev.expensetrackerhelios.model.ExpenseDatabase
import com.mardev.expensetrackerhelios.viewmodel.AuthViewModel
import com.mardev.expensetrackerhelios.viewmodel.AuthViewModelFactory

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(ExpenseDatabase.getInstance(requireContext()))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignIn.setOnClickListener {
            val username = binding.txtUsername.text?.toString()?.trim() ?: ""
            val password = binding.txtPassword.text?.toString()?.trim() ?: ""

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Username dan Password wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.signIn(username, password, requireContext()) { success, message ->
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    if (success) {
                        val prefs = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
                        prefs.edit().putString("username", username).apply()
                        findNavController().navigate(R.id.actionSignIntoItemExpense)
                    }
                }
            }
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }
}
