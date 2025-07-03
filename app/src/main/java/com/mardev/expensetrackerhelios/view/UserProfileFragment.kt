package com.mardev.expensetrackerhelios.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.mardev.expensetrackerhelios.R
import com.mardev.expensetrackerhelios.databinding.FragmentUserProfileBinding
import com.mardev.expensetrackerhelios.model.ExpenseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val db = ExpenseDatabase.getInstance(requireContext())
        val shared = requireActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = shared.getString("username", null)

        // 1. SET user ke binding agar TextView di XML keisi otomatis (Data Binding)
        GlobalScope.launch(Dispatchers.IO) {
            val user = db.userDao().getUserByUsername(username ?: "")
            launch(Dispatchers.Main) {
                if (user != null) {
                    binding.user = user  // akan mengisi semua TextView yang pakai @{user.xxx}
                }
            }
        }

        // 2. Ganti password
        binding.btnChangePw.setOnClickListener {
            val oldPw = binding.edtOldPw.text.toString()
            val newPw = binding.edtNewPw.text.toString()
            val repeatPw = binding.edtRepeatPw.text.toString()

            if (username == null) {
                Toast.makeText(requireContext(), "User tidak ditemukan!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (oldPw.isBlank() || newPw.isBlank() || repeatPw.isBlank()) {
                Toast.makeText(requireContext(), "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            GlobalScope.launch(Dispatchers.IO) {
                val user = db.userDao().getUserByUsername(username)
                if (user == null) {
                    launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "User tidak ditemukan!", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                if (user.password != oldPw) {
                    launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Password lama salah", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                if (newPw != repeatPw) {
                    launch(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Password baru tidak sama", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                db.userDao().updatePassword(user.id, newPw)
                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                    binding.edtOldPw.setText("")
                    binding.edtNewPw.setText("")
                    binding.edtRepeatPw.setText("")
                }
            }
        }

        // 3. Logout
        binding.btnSignOut.setOnClickListener {
            shared.edit().clear().apply()
            Navigation.findNavController(it).navigate(R.id.actionSignOut)
        }
    }
}
