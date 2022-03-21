package com.maricoolsapps.adminpart.ui.fragments

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.appComponents.MainActivity
import com.maricoolsapps.adminpart.databinding.FragmentSettingsBinding
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
        (activity as MainActivity).toolbar.title = "Settings"
        clickListeners()
    }

    private fun clickListeners() {
        binding.logout.setOnClickListener {
            auth.signOut()
            findNavController().popBackStack(R.id.loginFragment, true)
        }

        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        binding.notice.setOnClickListener {
            findNavController().navigate(R.id.noticeFragment)
        }

        binding.id.setOnClickListener {
            binding.id.showSnack("Your Id is ${auth.currentUser.uid}")
        }
        binding.id.setOnLongClickListener{
            val cm = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.text = auth.currentUser.uid
            requireActivity().showToast("Id is Copied to Clipboard")
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}