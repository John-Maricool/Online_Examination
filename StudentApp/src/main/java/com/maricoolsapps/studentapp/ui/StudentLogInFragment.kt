package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.resources.databinding.LoginLayoutBinding
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.utils.others.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudentLogInFragment : Fragment(R.layout.student_log_in_fragment) {

    private val model: StudentLogInViewModel by viewModels()
    private var _binding: LoginLayoutBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = LoginLayoutBinding.bind(view)

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.studentSignup)
        }

        binding.login.setOnClickListener { userLogin() }

        model.result.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    requireActivity().showToast(result.data.toString())
                    findNavController().navigate(R.id.mainFragment)
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBar.showSnack(result.message.toString())
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun userLogin() {
        val userEmail: String = binding.email.text.toString().trim()
        val userPassword: String = binding.password.text.toString().trim()

        if (!isEmailValid(userEmail)) {
            binding.email.error = "Email is required"
            binding.email.requestFocus()
            return
        }

        if (!isTextValid(userPassword)) {
            binding.password.error = "Password is required"
            binding.password.requestFocus()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        model.logInUser(userEmail, userPassword)
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            findNavController().navigate(R.id.mainFragment)
        }
    }
}





