package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.application.MainActivity
import com.maricoolsapps.studentapp.databinding.FragmentMainBinding
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.others.constants
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import com.maricoolsapps.utils.source.ServerUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    val binding: FragmentMainBinding get() = _binding!!
    private val model: MainViewModel by viewModels()
    lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        binding.str.setOnRefreshListener {
            model.checkIfPreviouslyRegistered()
        }

        observeLiveData()

        binding.start.setOnClickListener {
            model.checkIfUserIsActivated()
        }
        model.checkIfPreviouslyRegistered()
        binding.registerButton.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Register")

        val input = EditText(requireContext())
        input.hint = "Admin uid"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val text = input.text.toString().trim()
            if (text.isNotEmpty()) {
                binding.progrgess.visibility = View.VISIBLE
                model.registerForQuiz(text)
                dialog.dismiss()
            } else {
                Toast.makeText(activity, "Empty input", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun observeLiveData() {
        model.register.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    requireActivity().showToast(it.data!!)
                    binding.progrgess.visibility = View.GONE
                    model.checkIfPreviouslyRegistered()
                }
                Status.ERROR -> {
                    binding.progrgess.showSnack(it.message!!)
                    binding.progrgess.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.progrgess.visibility = View.VISIBLE
                }
            }
        }

        model.check.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.ERROR -> {
                    binding.str.isRefreshing = false
                    binding.registrationStatus.visibility = View.VISIBLE
                    binding.registrationStatus.text =
                        getString(R.string.registration_status_notRegistered)
                }

                Status.SUCCESS -> {
                    binding.str.isRefreshing = false
                    if (it.data == true) {
                        binding.registrationStatus.text =
                            getString(R.string.registration_status_registered)
                    } else {
                        binding.registrationStatus.text =
                            getString(R.string.registration_status_notRegistered)
                    }
                    binding.registrationStatus.visibility = View.VISIBLE

                }
                Status.LOADING -> {
                    binding.str.isRefreshing = true
                    binding.registrationStatus.visibility = View.VISIBLE
                    binding.registrationStatus.text = getString(R.string.registration_error_status)
                }
            }
        }

        model.ready.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.ERROR -> {
                    binding.str.isRefreshing = false
                    binding.registrationStatus.visibility = View.VISIBLE
                    binding.progrgess.showSnack(it.message!!)
                }

                Status.SUCCESS -> {
                    binding.str.isRefreshing = false
                    if (it.data == true) {
                        findNavController().navigate(R.id.mainQuiz)
                    } else {
                        binding.registrationStatus.text =
                            getString(R.string.not_ready)
                    }
                    binding.registrationStatus.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    binding.str.isRefreshing = true
                    binding.registrationStatus.visibility = View.VISIBLE
                }
            }
        }
    }

    /* override fun onStart() {
         super.onStart()
         *//* val name = user.getUserName()
         (activity as MainActivity).supportActionBar?.title = "Welcome $name"*//*
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}