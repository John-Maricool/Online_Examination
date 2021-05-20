package com.maricoolsapps.adminpart

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.maricoolsapps.adminpart.databinding.FragmentLoginBinding
import com.maricoolsapps.adminpart.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.multibindings.IntKey
import kotlinx.android.synthetic.main.fragment_sign_up.*
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
   @Inject lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)

        binding.login.setOnClickListener { userLogin() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun userLogin(){
        val username = binding.name.text.toString().trim()
        val userEmail: String = binding.email.text.toString().trim { it <= ' ' }
        val userPassword: String = binding.password.text.toString().trim { it <= ' ' }
        val confirmPassword: String = binding.confirmPassword.text.toString().trim { it <= ' ' }

        if (username.isEmpty()) {
            binding.name.error = "Name is required"
            binding.name.requestFocus()
            return
        }

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

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            binding.email.error = "Please enter a correct email"
            return
        }

        if (userPassword.length < 6) {
            binding.password.error = "Please enter a correct password"
            return
        }
        binding.progressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->
            binding.progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                val profile = UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()
                auth.currentUser?.updateProfile(profile)?.addOnSuccessListener {
                    startActivity(Intent(activity, AdminActivity::class.java))
                    activity?.finish()
                }

            } else {
                Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


}