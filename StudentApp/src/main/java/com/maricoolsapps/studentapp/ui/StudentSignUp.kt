package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.FragmentStudentSignupBinding
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.isEmailValid
import com.maricoolsapps.utils.others.isTextValid
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudentSignUp : Fragment(R.layout.fragment_student_signup) {

    private val model: SignUpViewModel by viewModels()

    private var _binding: FragmentStudentSignupBinding? = null
    val binding: FragmentStudentSignupBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStudentSignupBinding.bind(view)
        observeLiveData()
        binding.login.setOnClickListener {
            userLogin()
        }
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
                findNavController().navigate(R.id.mainFragment)
            }
        }
    }

    private fun userLogin() {
        val username = binding.name.text.toString().trim()
        val userEmail: String = binding.email.text.toString().trim()
        val userPassword: String = binding.password.text.toString().trim()
        val confirmPassword: String = binding.confirmPassword.text.toString().trim()
        val phoneNumber: String = binding.telNumber.text.toString().trim()

        if (!isTextValid(username) || !isTextValid(userEmail) || !isTextValid(userPassword) || !isTextValid(
                confirmPassword
            ) || !isTextValid(phoneNumber) || !isEmailValid(userEmail)
        ) {
            binding.progressBar.showSnack("Error with your entries")
            return
        }

        if (userPassword != confirmPassword) {
            binding.progressBar.showSnack("Error with your entries")
            return
        }

        val user = StudentUser(
            id = "id",
            name = username,
            email = userEmail,
            photoUri = null,
            number = phoneNumber,
            regNo = binding.regNumber.text.toString().trim(),
            isActivated = false,
            isRegistered = false,
            adminId = null,
            quizScore = null
        )
        model.signUser(user, userPassword)
    }

    override fun onDestroy() {
        super.onDestroy()
        model.serverUser.getAuthState().removeObservers(this)
        _binding = null
    }
}