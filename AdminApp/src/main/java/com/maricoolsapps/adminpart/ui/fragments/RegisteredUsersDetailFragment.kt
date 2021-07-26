package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.databinding.FragmentRegisteredUsersDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisteredUsersDetailFragment : Fragment(R.layout.fragment_registered_users_detail){

    private var _binding: FragmentRegisteredUsersDetailBinding? = null
    private val binding get() = _binding!!

    private val args: RegisteredUsersDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisteredUsersDetailBinding.bind(view)
        val user = args.user
        binding.apply {
            userName.append(user?.name)
            userEmail.append(user?.email)
            userPhoneNumber.append(user?.number)
            registered.append(user?.isRegistered.toString())
            activation.append(user?.isActivated.toString())
            regNo.append(user?.regNo)
            if (user?.quizScore != null) {
                quizScore.append("${user.quizScore}%")
            }else{
                quizScore.append("User has not taken Quiz yet")
            }

            Glide.with(requireActivity())
                    .load(user?.photoUri?.toUri())
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(profileImage)

        }
    }

}