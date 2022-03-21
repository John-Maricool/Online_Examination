package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.maricoolsapps.resources.databinding.ChangePasswordLayoutBinding
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.SignInDetails
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.others.isTextValid
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeEmailFragment : DialogFragment(R.layout.fragment_change_password) {

    private var _binding: ChangePasswordLayoutBinding? = null
    private val binding get() = _binding!!
    private val model: ChangeEmailViewModel by viewModels()
    private val args: ChangeEmailFragmentArgs by navArgs()

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
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBar.showSnack(it.message!!)
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