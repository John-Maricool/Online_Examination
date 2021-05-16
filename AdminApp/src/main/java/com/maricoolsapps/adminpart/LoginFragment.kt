package com.maricoolsapps.adminpart

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.adminpart.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

   private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)

        auth = FirebaseAuth.getInstance()
        binding.register.setOnClickListener{
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment2()
            findNavController().navigate(action)
        }

        binding.login.setOnClickListener { userLogin() }
     /*   binding.confirmEmail.setOnClickListener {
            auth.currentUser.sendEmailVerification().addOnCompleteListener {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
            }
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun userLogin(){
        val userEmail: String = binding.email.text.toString().trim { it <= ' ' }
        val userPassword: String = binding.password.text.toString().trim { it <= ' ' }

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
        binding.progressBar.setVisibility(View.VISIBLE)
        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener{ task ->
            binding.progressBar.setVisibility(View.GONE)
            if (task.isSuccessful) {
               startActivity(Intent(activity, AdminActivity::class.java))
                activity?.finish()
            } else {
                Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            startActivity(Intent(activity, AdminActivity::class.java))
            activity?.finish()
        }
    }
}