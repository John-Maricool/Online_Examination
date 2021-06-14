package com.maricoolsapps.adminpart.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.maricoolsapps.adminpart.ui.viewModels.LogInViewModel
import com.maricoolsapps.adminpart.appComponents.AdminActivity
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.databinding.FragmentLoginBinding
import com.maricoolsapps.adminpart.utils.MyServerDataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

   private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val model: LogInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)
        binding.register.setOnClickListener{
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment2()
            findNavController().navigate(action)
        }
        binding.login.setOnClickListener { userLogin() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
        model.logInUser(userEmail, userPassword).observe(viewLifecycleOwner, Observer {result ->
            when(result){
                is MyServerDataState.onLoaded -> {
                    startActivity(Intent(activity, AdminActivity::class.java))
                    activity?.finish()
                }
                is MyServerDataState.notLoaded ->{
                    Toast.makeText(activity, result.e.toString(), Toast.LENGTH_SHORT).show()

                }
                MyServerDataState.isLoading -> TODO()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        if (model.currentUser != null){
            startActivity(Intent(activity, AdminActivity::class.java))
            activity?.finish()
        }
    }
}