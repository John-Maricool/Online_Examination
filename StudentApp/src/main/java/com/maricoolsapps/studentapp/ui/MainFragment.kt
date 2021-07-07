package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObjects
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.FragmentMainBinding
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.others.constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    val binding: FragmentMainBinding get() = _binding!!

    private val model: MainViewModel by viewModels()

    lateinit var builder: AlertDialog.Builder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        binding.str.setOnRefreshListener {
            checkIfPreviouslyRegistered()
        }

        binding.start.setOnClickListener {
            downloadQuiz()
        }

        checkIfPreviouslyRegistered()

            binding.registerButton.setOnClickListener {
                showDialog()
            }
    }

    private fun downloadQuiz() {
        binding.progrgess.visibility = View.VISIBLE
        model.getQuizFromServer().observe(viewLifecycleOwner, { state ->
            when(state){
                is MyDataState.onLoaded -> {val snap = state.data as QuerySnapshot
                val result = snap.toObjects<ServerQuizDataModel>()
                    contimueProgress(result)
                }
                MyDataState.isLoading -> TODO()
                is MyDataState.notLoaded -> {binding.progrgess.visibility = View.GONE
                    Toast.makeText(activity, "Not Uploaded", Toast.LENGTH_LONG).show()}
            }
        })
    }

    private fun contimueProgress(result: List<ServerQuizDataModel>) {
        val local_res: List<RoomEntity> = model.mapToLocalDb(result)
        model.insertToLocalDatabase(local_res).observe(viewLifecycleOwner, {  bool ->
            when(bool){
                true -> {binding.progrgess.visibility = View.GONE
                    Toast.makeText(activity, "Uploaded Successfully", Toast.LENGTH_LONG).show()}
            }

        })
    }

    /*private fun checkIfTimeReached() {
        model.isTimeReached().observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {Toast.makeText(activity, "Time Reached", Toast.LENGTH_LONG).show()}
                false -> {Toast.makeText(activity, "Time never Reach", Toast.LENGTH_LONG).show()}
            }
        })
    }*/

    private fun checkIfPreviouslyRegistered() {
        binding.str.isRefreshing = true
        model.checkIfPreviouslyRegistered().observe(viewLifecycleOwner, { result ->
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

    builder.setPositiveButton("OK") { dialog, _ ->
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
    constants.admin_id = text
    model.registerForQuiz(text).observe(viewLifecycleOwner, Observer {
        when (it) {
            MyServerDataState.onLoaded -> {
                Toast.makeText(activity, "Successfully registered", Toast.LENGTH_LONG).show()
                binding.progrgess.visibility = View.GONE
                checkIfPreviouslyRegistered()
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