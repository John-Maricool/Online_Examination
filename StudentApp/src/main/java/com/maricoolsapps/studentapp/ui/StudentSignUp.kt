package com.maricoolsapps.studentapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.FragmentStudentSignupBinding
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.StudentUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class StudentSignUp : Fragment(R.layout.fragment_student_signup) {

    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var auth: FirebaseAuth

    private val model: SignUpViewModel by viewModels()

    lateinit var googleSignInClient: GoogleSignInClient
    private var _binding: FragmentStudentSignupBinding? = null
    val binding: FragmentStudentSignupBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account?.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("TAG", "Google sign in failed", e)
                }
            }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStudentSignupBinding.bind(view)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        binding.googleSignIn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
            }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        binding.progress.visibility = View.VISIBLE
                        val data = giveCorrectInputs()
                        if (data != null) {
                            model.createUser(data).observe(viewLifecycleOwner, {
                                when(it){
                                    MyServerDataState.onLoaded -> {
                                        binding.progress.visibility = View.GONE
                                        val action = StudentSignUpDirections.actionStudentSignupToMainFragment()
                                        findNavController().navigate(action)
                                    }
                                    is MyServerDataState.notLoaded -> {
                                        binding.progress.visibility = View.GONE
                                        Toast.makeText(activity, it.e.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                    MyServerDataState.isLoading -> TODO()
                                }
                            })
                        }else{
                            Toast.makeText(activity, "Error Creating Account", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_LONG).show()
                    }
                }
    }

    private fun giveCorrectInputs(): StudentUser? {
        val name = binding.name.text.toString().trim()
        val email = binding.mail.text.toString().trim()
        val phoneNumber = binding.number.text.toString().trim()
        val regNo = binding.regNumber.text.toString().trim()

        return if(name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()){
            null
        }else{
            StudentUser(
                    id = auth.currentUser.uid,
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