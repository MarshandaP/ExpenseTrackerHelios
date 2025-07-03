//package com.mardev.expensetrackerhelios.view
//
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.navigation.Navigation
//import com.mardev.expensetrackerhelios.databinding.FragmentUserProfileBinding
//import com.mardev.expensetrackerhelios.model.ExpenseDatabase
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//
//class UserProfileFragment : Fragment() {
//
//    private lateinit var binding: FragmentUserProfileBinding
//    private var currentUserId: Int = -1
//    private var currentPassword: String = ""
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val db = ExpenseDatabase.getInstance(requireContext())
//        val shared = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
//        currentUserId = shared.getInt("user_id", -1)
//
//        // Ambil password user sekarang dari database
//        GlobalScope.launch(Dispatchers.IO) {
//            val user = db.userDao().selectUser(currentUserId)
//            currentPassword = user.password
//        }
//
//        binding.btnChangePw.setOnClickListener {
//            val oldPw = binding.edtOldPw.text.toString()
//            val newPw = binding.edtNewPw.text.toString()
//            val repeatPw = binding.edtRepeatPw.text.toString()
//
//            if (oldPw != currentPassword) {
//                Toast.makeText(requireContext(), "Password lama salah", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            if (newPw != repeatPw) {
//                Toast.makeText(requireContext(), "Password baru tidak sama", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            GlobalScope.launch(Dispatchers.IO) {
//                db.userDao().updatePassword(currentUserId, newPw)
//                launch(Dispatchers.Main) {
//                    Toast.makeText(requireContext(), "Password berhasil diubah", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//        binding.btnSignOut.setOnClickListener {
//            shared.edit().clear().apply()
//            Navigation.findNavController(it).navigate(UserProfileFragmentDirections.actionSignOut())
//        }
//    }
//}
