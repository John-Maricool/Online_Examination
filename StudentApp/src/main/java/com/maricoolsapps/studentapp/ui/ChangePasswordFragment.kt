package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.maricoolsapps.resources.databinding.ChangePasswordLayoutBinding
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.FragmentChangePasswordBinding
import com.maricoolsapps.utils.datastate.MyServerDataState

class ChangePasswordFragment : DialogFragment(R.layout.fragment_change_password) {

    private var _binding: ChangePasswordLayoutBinding? = null
    private val binding get() = _binding!!

    private val model: ChangePasswordViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ChangePasswordLayoutBinding.bind(view)
        binding.reset.setOnClickListener { resetPassword() }
    }

    private fun resetPassword() {
        val oldPassword = binding.OldPassword.text.toString().trim()
        val newPassword = binding.password.text.toString().trim()

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
        if (oldPassword.length < 6 || newPassword.length < 6) {
            binding.password.error = "Please enter a correct password"
            binding.OldPassword.error = "Please enter a correct password"
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