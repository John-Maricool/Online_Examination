package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.ui.viewModels.SignUpViewModel
import com.maricoolsapps.adminpart.databinding.FragmentSignUpBinding
import com.maricoolsapps.utils.others.isEmailValid
import com.maricoolsapps.utils.others.isTextValid
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val model: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)

        binding.login.setOnClickListener { userLogin() }
        observeLiveData()
    }

    private fun observeLiveData() {
        model.logginIn.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.progressBar.showSnack("Error")
            }
        }

        model.success.observe(viewLifecycleOwner) {
            if (it != null) {
                requireActivity().showToast(it)
                findNavController().navigate(R.id.registeredUsersFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        model.serverUser.getAuthState().removeObservers(this)
        _binding = null
    }

    private fun userLogin() {
        val username = binding.name.text.toString().trim()
        val userEmail: String = binding.email.text.toString().trim()
        val userPassword: String = binding.password.text.toString().trim()
        val confirmPassword: String = binding.confirmPassword.text.toString().trim()

        if (!isTextValid(userEmail) || !isTextValid(userPassword) || !isTextValid(confirmPassword)
            || userPassword != confirmPassword || !isEmailValid(userEmail)
        ) {
            binding.progressBar.showSnack("Error in the entries. Check your entries")
            return
        }

        model.signUser(username, userEmail, userPassword)
    }
}