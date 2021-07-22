package com.maricoolsapps.studentapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.FragmentProfileBinding
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.others.constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val model: ProfileViewModel by viewModels()

    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent_data = result.data?.data
                binding.progress.visibility = View.VISIBLE
                if (intent_data != null) {
                    model.changeProfilePhoto(intent_data).observe(viewLifecycleOwner, Observer { res ->
                        when (res) {
                            is MyServerDataState.onLoaded -> {
                                Toast.makeText(activity, "Image Uploaded successfully", Toast.LENGTH_LONG).show()
                                binding.progress.visibility = View.GONE
                                Glide.with(requireActivity())
                                        .load(intent_data)
                                        .circleCrop()
                                        .into(binding.profileImage)
                            }
                            is MyServerDataState.notLoaded -> {
                                binding.progress.visibility = View.GONE
                                Toast.makeText(activity, res.e.toString(), Toast.LENGTH_LONG).show()
                            }
                            MyServerDataState.isLoading -> TODO()
                        }
                    })
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        model.profilePhoto.observe(viewLifecycleOwner, { uri ->
            Glide.with(requireActivity())
                    .load(uri)
                    .circleCrop()
                    .placeholder(R.drawable.profile)
                    .into(binding.profileImage)
        })

        binding.imageChooser.setOnClickListener {
            chooseImage()
        }

        binding.changeUsername.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToChangeProfileFragment(constants.username)
            findNavController().navigate(action)
        }

        binding.changeEmail.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToChangeProfileFragment(constants.mail)
            findNavController().navigate(action)
        }

        binding.changePassword.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment()
            findNavController().navigate(action)
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}