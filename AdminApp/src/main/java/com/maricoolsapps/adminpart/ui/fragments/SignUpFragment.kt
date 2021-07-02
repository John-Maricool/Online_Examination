package com.maricoolsapps.adminpart.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.adminpart.appComponents.AdminActivity
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.ui.viewModels.SignUpViewModel
import com.maricoolsapps.adminpart.databinding.FragmentSignUpBinding
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.AdminUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var auth: FirebaseAuth
    private val model: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)

        binding.login.setOnClickListener { userLogin() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun userLogin() {
        val username = binding.name.text.toString().trim()
        val userEmail: String = binding.email.text.toString().trim()
        val userPassword: String = binding.password.text.toString().trim()
        val confirmPassword: String = binding.confirmPassword.text.toString().trim()

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

        if (confirmPassword != userPassword) {
            binding.confirmPassword.error = "Passwords dosen't match"
            binding.confirmPassword.requestFocus()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        model.createUser(userEmail, userPassword, username).observe(viewLifecycleOwner, Observer {result ->
            when (result) {
                is MyServerDataState.onLoaded -> {
                    val user = AdminUser(username, userEmail)
                    sendDataToFirestore(user)
                }

                is MyServerDataState.notLoaded -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, result.e.toString(), Toast.LENGTH_LONG).show()
                }
                MyServerDataState.isLoading -> TODO()
            }
        })
    }

    private fun sendDataToFirestore(user: AdminUser) {
            model.sendToFirestore(user, auth).observe(viewLifecycleOwner, Observer {
                when(it){
                    MyServerDataState.onLoaded ->{
                        binding.progressBar.visibility = View.GONE
                        startActivity(Intent(activity, AdminActivity::class.java))
                        activity?.finish()}

                    is MyServerDataState.notLoaded -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.e.toString(), Toast.LENGTH_LONG).show()
                    }
                    MyServerDataState.isLoading -> TODO()
                }
            })
        }
}