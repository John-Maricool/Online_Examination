package com.maricoolsapps.adminpart

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.maricoolsapps.adminpart.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var auth: FirebaseAuth
    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                val intent_data  = result.data?.data
                binding.progress.visibility = View.VISIBLE
                val profile = UserProfileChangeRequest.Builder()
                        .setPhotoUri(intent_data)
                        .build()
                auth.currentUser?.updateProfile(profile)?.addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(activity, "Image Uploaded successfully", Toast.LENGTH_LONG).show()
                        binding.progress.visibility = View.GONE
                        Glide.with(requireActivity())
                                .load(auth.currentUser?.photoUrl)
                                .circleCrop()
                                .into(binding.profileImage)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        Glide.with(requireActivity())
                .load(auth.currentUser?.photoUrl)
                .circleCrop()
                .placeholder(R.drawable.profile)
                .into(binding.profileImage)

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

       /* if (auth.currentUser?.isEmailVerified == true){
            binding.confirmEmail.text = "Email Verified"
            binding.confirmEmail.isEnabled = false
        }else{
            binding.confirmEmail.setOnClickListener {
            auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                if (it.isComplete){
                    binding.confirmEmail.text = "Email Verified"
                    binding.confirmEmail.isEnabled = false
                }
                Toast.makeText(activity, "Email Verification sent", Toast.LENGTH_LONG).show()
            }?.addOnFailureListener {
                Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
            }
            }*/
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        resultLauncher.launch(intent)
         }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}