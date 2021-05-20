package com.maricoolsapps.adminpart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.adminpart.databinding.FragmentChangePasswordDialogBinding
import com.maricoolsapps.adminpart.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordDialog : DialogFragment(R.layout.fragment_change_password_dialog) {

    private var _binding: FragmentChangePasswordDialogBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChangePasswordDialogBinding.bind(view)
        binding.reset.setOnClickListener { resetPassword() }
    }

    private fun resetPassword() {
        val oldPassword = binding.OldPassword.text.toString().trim()
        val newPassword = binding.password.text.toString().trim()
        val confirmNewPassword = binding.confirmPassword.text.toString().trim()

        if (oldPassword.isEmpty()) {
            binding.OldPassword.error = "Password is required"
            binding.OldPassword.requestFocus()
            return
        }
        if (newPassword.isEmpty()) {
            binding.password.error = "Password is required"
            binding.password.requestFocus()
            return
        }
        if (confirmNewPassword.isEmpty()) {
            binding.confirmPassword.error = "Password is required"
            binding.confirmPassword.requestFocus()
            return
        }
        if (oldPassword.length < 6 || newPassword.length < 6 || confirmNewPassword.length < 6) {
            binding.password.error = "Please enter a correct password"
            binding.OldPassword.error = "Please enter a correct password"
            binding.confirmPassword.error = "Please enter a correct password"
            return
        }
        if (newPassword != newPassword){
            binding.confirmPassword.error = "Please enter a correct password"
            return
        }
        binding.progressBar.visibility = View.VISIBLE
       val mail: String = auth.currentUser?.email.toString()
        val credentials = EmailAuthProvider.getCredential(mail, oldPassword)
        auth.currentUser?.reauthenticate(credentials)?.addOnCompleteListener {
            if (it.isSuccessful){
                auth.currentUser?.updatePassword(newPassword)?.addOnCompleteListener{
                    if (it.isSuccessful){
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, "Password Updated", Toast.LENGTH_LONG).show()
                    }else{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, "Error Updating password", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}