package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.appComponents.MainActivity
import com.maricoolsapps.adminpart.databinding.FragmentRegisteredUsersDetailBinding
import com.maricoolsapps.adminpart.ui.viewModels.RegisteredUsersDetailViewModel
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisteredUsersDetailFragment : Fragment(R.layout.fragment_registered_users_detail) {

    private var _binding: FragmentRegisteredUsersDetailBinding? = null
    private val binding get() = _binding!!

    private val model: RegisteredUsersDetailViewModel by viewModels()
    private val args: RegisteredUsersDetailFragmentArgs by navArgs()
    lateinit var user: StudentUser

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisteredUsersDetailBinding.bind(view)
        setHasOptionsMenu(true)
        model.getStudent(args.user)
        observeLiveData()
        clickViews()
    }

    private fun clickViews() {
        binding.call.setOnClickListener {
            if (::user.isInitialized)
            startActivity(model.callStudent(user.number!!))
        }
        binding.message.setOnClickListener {
            //message user
        }
    }

    private fun observeLiveData() {
        model.result.observe(viewLifecycleOwner) {
            if (it != null) {
                 user = it
                binding.apply {
                    (activity as MainActivity).toolbar.title = user.name
                    userEmail.append( "\n ${user.email}")
                    userPhoneNumber.append("\n ${user.number}")
                    if (!user.isActivated) {
                        activation.append(" \n ${user.name} cannot take quiz no")
                    } else {
                        activation.append(" \n ${user.name} is eligible to take quiz")
                    }
                    regNo.append(user.regNo)
                    if (user.quizScore != null) {
                        quizScore.append(" \n ${user.quizScore}%")
                    }
                    Glide.with (requireActivity())
                        .load(user.photoUri?.toUri())
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(profileImage)
                }
            }
        }
        model.done.observe(viewLifecycleOwner) {
            when (it?.status) {
                Status.SUCCESS -> {
                    requireActivity().showToast(it.data!!)
                    binding.progressBar.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.progressBar.showSnack(it.message!!)
                    binding.progressBar.visibility = View.GONE
                }
                Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activate_user, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.activate -> {
                model.activateUser(args.user)
            }
            R.id.deactivate -> {
                model.deactivateUser(args.user)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}