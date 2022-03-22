package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.maricoolsapps.room_library.room.QuizResultEntity
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.FragmentQuizResultBinding
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.models.StudentUser
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.others.showSnack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizResultFragment : Fragment(R.layout.fragment_quiz_result) {

    private var _binding: FragmentQuizResultBinding? = null
    private val binding get() = _binding!!

    private val model: QuizResultViewModel by viewModels()
    var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.mainFragment)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizResultBinding.bind(view)
        binding.save.setOnClickListener {
            saveResult()
        }
        observeLiveData()

    }

    private fun observeLiveData() {
        model.result.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> binding.progressBar.visibility = View.VISIBLE
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBar.showSnack(it.message!!)
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    updateView(it.data as StudentUser)
                }
            }
        }
    }

    private fun saveResult() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Save")
        alertDialogBuilder.setMessage("Save result")
        val input = EditText(requireContext())
        input.hint = "Enter the name to save quiz with"
        alertDialogBuilder.setView(input)
            .setPositiveButton("Save") { dialog, _ ->
                val text = input.text.toString().trim()
                if (text.isNotEmpty()) {
                    val result = QuizResultEntity(text, score)
                    val job = model.addResult(result)
                    if (job.isCompleted) {
                        Toast.makeText(activity, "Saved", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                } else {
                    Toast.makeText(activity, "Empty input", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
            }.setNegativeButton("Exit") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.show()
    }

    private fun updateView(studentUser: StudentUser) {
        binding.apply {
            progressBar.visibility = View.GONE
            textCongrats.append("\n ${studentUser.name}")

            score = studentUser.quizScore!!
            textScore.append("${studentUser.quizScore.toString()} %")

            Glide.with(requireActivity())
                .load(studentUser.photoUri?.toUri())
                .circleCrop()
                .placeholder(R.drawable.profile)
                .into(binding.profileImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}