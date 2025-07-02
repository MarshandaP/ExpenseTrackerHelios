package com.mardev.expensetrackerhelios.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mardev.expensetrackerhelios.databinding.FragmentSignUpBinding
import com.mardev.expensetrackerhelios.model.ExpenseDatabase
import com.mardev.expensetrackerhelios.model.User
import com.mardev.expensetrackerhelios.viewmodel.AuthViewModel
import com.mardev.expensetrackerhelios.viewmodel.AuthViewModelFactory

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(ExpenseDatabase.getInstance(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            val username = binding.txtUsername.text?.toString()?.trim() ?: ""
            val firstName = binding.txtFirstName.text?.toString()?.trim() ?: ""
            val lastName = binding.txtLastName.text?.toString()?.trim() ?: ""
            val password = binding.txtPassword.text?.toString() ?: ""
            val rptPassword = binding.txtRptPass.text?.toString() ?: ""

            // Validasi input
            if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty() || rptPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(
                username = username,
                firstName = firstName,
                lastName = lastName,
                password = password
            )

            viewModel.signUp(user, rptPassword, requireContext()) { success, message ->
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    if (success) {
                        // Navigasi ke SignInFragment (aktifkan jika sudah setup nav graph)
                        // findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                    }
                }
            }
        }

        binding.back.setOnClickListener {
            // Navigasi balik ke SignInFragment
            // findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
