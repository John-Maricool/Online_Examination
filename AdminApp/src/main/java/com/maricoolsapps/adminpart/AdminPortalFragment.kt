package com.maricoolsapps.adminpart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.adminpart.databinding.FragmentAdminProtalBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Suppress("DEPRECATION")
class AdminPortalFragment : Fragment(R.layout.fragment_admin_protal) {

    private var _binding: FragmentAdminProtalBinding? = null
    private val binding get() = _binding!!

    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        _binding = FragmentAdminProtalBinding.bind(view)
        binding.createTest.setOnClickListener{
            val action = AdminPortalFragmentDirections.actionAdminPortalFragmentToQuizArrangement()
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