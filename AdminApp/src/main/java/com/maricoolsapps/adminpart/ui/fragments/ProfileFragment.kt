package com.maricoolsapps.adminpart.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.appComponents.MainActivity
import com.maricoolsapps.adminpart.databinding.FragmentProfileBinding
import com.maricoolsapps.adminpart.ui.viewModels.ProfileViewModel
import com.maricoolsapps.resources.databinding.ProfileLayoutBinding
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.AdminUser
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.others.constants
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: ProfileLayoutBinding? = null
    private val binding get() = _binding!!
    lateinit var admin: AdminUser

    private val model: ProfileViewModel by viewModels()

    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intent_data = result.data?.data
                    binding.progress.visibility = View.VISIBLE
                    if (intent_data != null) {
                        model.changeProfilePhoto(intent_data.toString())
                        Glide.with(requireActivity())
                            .load(intent_data)
                            .centerCrop()
                            .into(binding.profileImage)
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ProfileLayoutBinding.bind(view)
        (activity as MainActivity).toolbar.title = "Profile"
        clickViews()
        observeLiveData()
    }

    private fun observeLiveData() {
        model.profilePhoto.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    requireActivity().showToast(res.data!!)
                    binding.progress.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    binding.progress.showSnack(res.message!!)
                }
                Status.LOADING -> binding.progress.visibility = View.VISIBLE
            }
        }
        model.admin.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    admin = res.data!!
                    Log.d("ssjs", admin.toString())
                    binding.progress.visibility = View.GONE
                    Glide.with(requireActivity())
                        .load(admin.photoUri)
                        .centerCrop()
                        .into(binding.profileImage)
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    binding.progress.showSnack(res.message!!)
                }
                Status.LOADING -> binding.progress.visibility = View.VISIBLE
            }
        }
    }

    private fun clickViews() {
        binding.imageChooser.setOnClickListener {
            chooseImage()
        }

        binding.changeUsername.setOnClickListener {
            if (this::admin.isInitialized) {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToChangeProfileDialog(admin.name)
                findNavController().navigate(action)
            }
        }

        binding.changeEmail.setOnClickListener {
            if (this::admin.isInitialized) {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToChangeEmail(admin.email)
                findNavController().navigate(action)
            }
        }
    }

    private fun chooseImage() {
        resultLauncher.launch(model.chooseImage())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
