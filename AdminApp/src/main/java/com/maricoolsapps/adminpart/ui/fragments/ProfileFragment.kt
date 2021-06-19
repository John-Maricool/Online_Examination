package com.maricoolsapps.adminpart.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
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
import com.maricoolsapps.adminpart.databinding.FragmentProfileBinding
import com.maricoolsapps.adminpart.ui.viewModels.ProfileViewModel
import com.maricoolsapps.utils.datastate.MyServerDataState
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
                    model.changeProfilePhoto(intent_data).observe(viewLifecycleOwner, Observer { result ->
                        when (result) {
                            is MyServerDataState.onLoaded -> {
                                model.updateProfileInFirestore(intent_data.toString()).observe(viewLifecycleOwner, Observer {inner_result->
                                    when(inner_result){
                                        MyServerDataState.onLoaded ->{
                                            Toast.makeText(activity, "Image Uploaded successfully", Toast.LENGTH_LONG).show()
                                            binding.progress.visibility = View.GONE
                                            Glide.with(requireActivity())
                                                    .load(intent_data)
                                                    .circleCrop()
                                                    .into(binding.profileImage)
                                        }
                                        is MyServerDataState.notLoaded ->{
                                            binding.progress.visibility = View.GONE
                                            Toast.makeText(activity, inner_result.e.toString(), Toast.LENGTH_LONG).show()
                                        }
                                        MyServerDataState.isLoading -> TODO()
                                    }
                                })

                            }
                            is MyServerDataState.notLoaded -> {
                                binding.progress.visibility = View.GONE
                                Toast.makeText(activity, result.e.toString(), Toast.LENGTH_LONG).show()
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

        model.profilePhoto.observe(viewLifecycleOwner, Observer { uri ->
            Glide.with(requireActivity())
                    .load(uri?.toUri())
                    .circleCrop()
                    .placeholder(R.drawable.profile)
                    .into(binding.profileImage)
        })

        binding.imageChooser.setOnClickListener {
            chooseImage()
        }

        binding.changeUsername.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToChangeNameDialog()
            findNavController().navigate(action)
        }

        binding.changePassword.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToChangePasswordDialog()
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