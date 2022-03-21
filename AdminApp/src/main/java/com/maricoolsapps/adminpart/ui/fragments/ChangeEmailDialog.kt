package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maricoolsapps.adminpart.ui.viewModels.ChangeEmailViewModel
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.resources.databinding.ChangePasswordLayoutBinding
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.SignInDetails
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.others.isTextValid
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeEmailDialog : DialogFragment(R.layout.fragment_change_password_dialog) {

    private var _binding: ChangePasswordLayoutBinding? = null
    private val binding get() = _binding!!

    private val model: ChangeEmailViewModel by viewModels()
    private val args: ChangeEmailDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ChangePasswordLayoutBinding.bind(view)
        binding.oldEmail.setText(args.email)
        binding.reset.setOnClickListener { resetEmail() }
        observeLiveData()
    }

    private fun observeLiveData() {
        model.changed.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    requireActivity().showToast(it.data!!)
                    dismiss()
                    findNavController().navigate(R.id.profileFragment)
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBar.showSnack(it.message!!)
                    dismiss()
                }
                Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun resetEmail() {
        val oldEmail = binding.oldEmail.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val newEmail = binding.newEmail.text.toString().trim()

        if (!isTextValid(oldEmail) || !isTextValid(password) || !isTextValid(newEmail)) {
            binding.progressBar.showSnack("Error in your Entries, check them well")
            return
        }
        val details = SignInDetails(oldEmail, password)
        model.changeEmail(details, newEmail)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}