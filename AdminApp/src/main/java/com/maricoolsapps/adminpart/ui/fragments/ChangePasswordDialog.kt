package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.maricoolsapps.adminpart.ui.viewModels.ChangePasswordViewModel
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.databinding.FragmentChangePasswordDialogBinding
import com.maricoolsapps.adminpart.utils.MyServerDataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordDialog : DialogFragment(R.layout.fragment_change_password_dialog) {

    private var _binding: FragmentChangePasswordDialogBinding? = null
    private val binding get() = _binding!!

    private val model: ChangePasswordViewModel by viewModels()

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
        if (newPassword != confirmNewPassword) {
            binding.confirmPassword.error = "Please enter a correct password"
            return
        }
        binding.progressBar.visibility = View.VISIBLE
        model.reAuthenticate(oldPassword).observe(viewLifecycleOwner, Observer { result ->

            when (result) {
                is MyServerDataState.onLoaded -> {
                    model.changePassword(newPassword).observe(viewLifecycleOwner, Observer { second_result ->
                        when (second_result) {
                            is MyServerDataState.onLoaded -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(activity, "Updated", Toast.LENGTH_LONG).show()
                                dismiss()
                            }

                            is MyServerDataState.notLoaded -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(activity, second_result.e.toString(), Toast.LENGTH_LONG).show()
                                dismiss()
                            }
                        }
                    })
                }

                is MyServerDataState.notLoaded -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, result.e.toString(), Toast.LENGTH_LONG).show()
                    dismiss()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}