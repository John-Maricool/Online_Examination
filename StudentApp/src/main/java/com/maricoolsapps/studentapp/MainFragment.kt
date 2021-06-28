package com.maricoolsapps.studentapp

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.maricoolsapps.studentapp.databinding.FragmentMainBinding
import com.maricoolsapps.utils.datastate.MyServerDataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    val binding: FragmentMainBinding get() = _binding!!

    private val model: MainViewModel by viewModels()

    lateinit var builder: AlertDialog.Builder
    lateinit var bar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        binding.str.setOnRefreshListener {
            checkIfPreviouslyRegistered()
        }

        checkIfPreviouslyRegistered()

            binding.registerButton.setOnClickListener {
                showDialog()
            }
    }

    private fun checkIfPreviouslyRegistered() {
        binding.str.isRefreshing = true
        model.checkIfPreviouslyRegistered().observe(viewLifecycleOwner, Observer { result ->
        when (result) {
            false -> {
                binding.str.isRefreshing = false
                binding.start.visibility = View.GONE
                binding.registerButton.visibility = View.VISIBLE
                binding.registrationStatus.visibility = View.VISIBLE
                binding.registrationStatus.text = getString(R.string.registration_status_notRegistered)
            }

            true -> {
                binding.str.isRefreshing = false
                binding.registerButton.visibility = View.GONE
                binding.start.visibility = View.VISIBLE
                binding.registrationStatus.visibility = View.VISIBLE
                binding.registrationStatus.text = getString(R.string.registration_status_registered)
            }
            null -> {
                binding.str.isRefreshing = false
                binding.registrationStatus.visibility = View.VISIBLE
                binding.registrationStatus.text = getString(R.string.registration_error_status)
            }
        }
    })
}

private fun showDialog() {
    builder = AlertDialog.Builder(requireContext())
    builder.setTitle("Register")

    val input = EditText(requireContext())
    input.hint = "Admin uid"
    input.inputType = InputType.TYPE_CLASS_TEXT
    builder.setView(input)

    builder.setPositiveButton("OK") { dialog, which ->
        val text = input.text.toString().trim()
        if (text.isNotEmpty()) {
            binding.progrgess.visibility = View.VISIBLE
            checkIfRegistered(text)
            dialog.dismiss()
        } else {
            Toast.makeText(activity, "Empty input", Toast.LENGTH_SHORT).show()
            return@setPositiveButton
        }
    }
    builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
    builder.show()
}

private fun checkIfRegistered(text: String) {
    model.checkIfAdminDocExist(text).observe(viewLifecycleOwner, Observer { result ->
        when (result) {
            true -> registerForQuiz(text)
            false -> {
                Toast.makeText(activity, "This Admin uid dosen't exist", Toast.LENGTH_LONG).show()
                binding.progrgess.visibility = View.GONE
            }
            null -> {
                Toast.makeText(activity, "Check your Internet Connection and try again", Toast.LENGTH_LONG).show()
                binding.progrgess.visibility = View.GONE
            }
        }
    })
}

private fun registerForQuiz(text: String) {
    model.registerForQuiz(text).observe(viewLifecycleOwner, Observer {
        when (it) {
            MyServerDataState.onLoaded -> {
                Toast.makeText(activity, "Successfully registered", Toast.LENGTH_LONG).show()
                binding.progrgess.visibility = View.GONE
            }
            is MyServerDataState.notLoaded -> {
                Toast.makeText(activity, "Check your internet connection and try again", Toast.LENGTH_LONG).show()
                binding.progrgess.visibility = View.GONE
            }
            MyServerDataState.isLoading -> {
            }
        }
    })
}

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}
}