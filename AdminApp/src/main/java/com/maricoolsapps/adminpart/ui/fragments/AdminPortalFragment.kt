package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.appComponents.AdminActivity
import com.maricoolsapps.adminpart.databinding.FragmentAdminProtalBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@Suppress("DEPRECATION")
class AdminPortalFragment : Fragment(R.layout.fragment_admin_protal) {

    private var _binding: FragmentAdminProtalBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminProtalBinding.bind(view)

        binding.createTest.setOnClickListener{
            val action = AdminPortalFragmentDirections.actionAdminPortalFragmentToQuizArrangement(null)
            findNavController().navigate(action)
        }

        binding.uploadQuiz.setOnClickListener {
            val action = AdminPortalFragmentDirections.actionAdminPortalFragmentToUploadQuizFragment()
            findNavController().navigate(action)
        }

        binding.registeredUsers.setOnClickListener {
            val action = AdminPortalFragmentDirections.actionAdminPortalFragmentToRegisteredUsersFragment()
            findNavController().navigate(action)
        }

        binding.savedQuiz.setOnClickListener {
            val action = AdminPortalFragmentDirections.actionAdminPortalFragmentToSavedQuizFragment()
            findNavController().navigate(action)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val name = auth.currentUser?.displayName
        (activity as AdminActivity).supportActionBar?.title = "Welcome $name"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}