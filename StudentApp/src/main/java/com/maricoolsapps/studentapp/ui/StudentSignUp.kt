package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.FragmentStudentSignupBinding
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.StudentUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudentSignUp : Fragment(R.layout.fragment_student_signup) {

    @Inject
    lateinit var auth: FirebaseAuth

    private val model: SignUpViewModel by viewModels()

    private var _binding: FragmentStudentSignupBinding? = null
    val binding: FragmentStudentSignupBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStudentSignupBinding.bind(view)
        binding.login.setOnClickListener {
            userLogin()
        }
    }

    private fun userLogin() {
        val username = binding.name.text.toString().trim()
        val userEmail: String = binding.email.text.toString().trim()
        val userPassword: String = binding.password.text.toString().trim()
        val confirmPassword: String = binding.confirmPassword.text.toString().trim()
        val phoneNumber: String = binding.telNumber.text.toString().trim()

        if (username.isEmpty()) {
            binding.name.error = "Name is required"
            binding.name.requestFocus()
            return
        }

        if (userEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            binding.email.error = "Email Error"
            binding.email.requestFocus()
            return
        }


        if (userPassword.isEmpty() || userPassword.length < 6) {
            binding.password.error = "Password Error"
            binding.password.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.confirmPassword.error = "Password is required"
            binding.confirmPassword.requestFocus()
            return
        }

        if (phoneNumber.isEmpty()) {
            binding.confirmPassword.error = "Phone Number is required"
            binding.confirmPassword.requestFocus()
            return
        }

        if (confirmPassword != userPassword) {
            binding.confirmPassword.error = "Passwords dosen't match"
            binding.confirmPassword.requestFocus()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
       val user =  giveCorrectInputs()
        if (user != null) {
            model.signUser(user, userPassword, auth).observe(viewLifecycleOwner, {
                when(it){
                    is MyServerDataState.onLoaded -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, "Completed Registration", Toast.LENGTH_LONG).show()
                        val action = StudentSignUpDirections.actionStudentSignupToMainFragment()
                        findNavController().navigate(action)
                    }
                    MyServerDataState.isLoading -> TODO()
                    is MyServerDataState.notLoaded -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.e.toString(), Toast.LENGTH_LONG).show()

                    }
                }
            })
        }
        else{
            return
        }
    }

    private fun giveCorrectInputs(): StudentUser? {
        val name = binding.name.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val phoneNumber = binding.telNumber.text.toString().trim()
        val regNo = binding.regNumber.text.toString().trim()

        return if(name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()){
            null
        }else{
            StudentUser(
                    id = "id",
                    name = name,
                    email = email,
                    number = phoneNumber,
                    regNo = regNo,
                    isActivated = true,
                    isRegistered = false
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            val action = StudentSignUpDirections.actionStudentSignupToMainFragment()
            findNavController().navigate(action)
        }
    }
}