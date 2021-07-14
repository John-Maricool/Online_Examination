package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.StudentLogInFragmentBinding
import com.maricoolsapps.utils.datastate.MyServerDataState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudentLogInFragment : Fragment(R.layout.student_log_in_fragment), FirebaseAuth.AuthStateListener {

    private  val viewModel: StudentLogInViewModel by viewModels()

    private var _binding: StudentLogInFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth.addAuthStateListener (this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = StudentLogInFragmentBinding.bind(view)
        binding.register.setOnClickListener{
            val action = StudentLogInFragmentDirections.actionStudentLogInFragmentToStudentSignup()
            findNavController().navigate(action)
        }
        binding.login.setOnClickListener { userLogin() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        auth.removeAuthStateListener(this)
    }

    private fun userLogin(){
        val userEmail: String = binding.email.text.toString().trim()
        val userPassword: String = binding.password.text.toString().trim()

        if (userEmail.isEmpty()) {
            binding.email.error = "Email is required"
            binding.email.requestFocus()
            return
        }

        if (userPassword.isEmpty()) {
            binding.password.error = "Password is required"
            binding.password.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            binding.email.error = "Please enter a correct email"
            return
        }

        if (userPassword.length < 6) {
            binding.password.error = "Please enter a correct password"
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        viewModel.logInUser(userEmail, userPassword).observe(viewLifecycleOwner) { result ->
            when (result) {
                is MyServerDataState.onLoaded -> {
                    binding.progressBar.visibility = View.GONE
                     val action = StudentLogInFragmentDirections.actionStudentLogInFragmentToMainFragment()
                    findNavController().navigate(action)
                    activity?.finish()
                }
                is MyServerDataState.notLoaded -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, result.e.toString(), Toast.LENGTH_SHORT).show()
                }
                MyServerDataState.isLoading -> TODO()
            }
        }
    }

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        if (auth.currentUser != null){
            findNavController().navigate(R.id.mainFragment)
        }
    }
}