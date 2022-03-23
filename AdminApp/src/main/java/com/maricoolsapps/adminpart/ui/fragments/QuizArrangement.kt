package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.appComponents.MainActivity
import com.maricoolsapps.adminpart.databinding.FragmentQuizArrangementBinding
import com.maricoolsapps.adminpart.ui.viewModels.QuizArrangementViewModel
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QuizArrangement : Fragment(R.layout.fragment_quiz_arrangement) {

    private var _binding: FragmentQuizArrangementBinding? = null
    private val binding get() = _binding!!
   private val viewModel: QuizArrangementViewModel by viewModels()
    private val args: QuizArrangementArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizArrangementBinding.bind(view)
        (activity as MainActivity).toolbar.title = "Create Test"
        if (args.items == null) {
            binding.update.visibility = View.GONE
        } else {
            binding.add.visibility = View.GONE
            binding.update.visibility = View.VISIBLE
            updateView(args.items!!)
        }

        binding.update.setOnClickListener {
            send()
        }

        binding.add.setOnClickListener {
            send()
            clearAllInputs()
        }
    }

    private fun updateView(items: RoomEntity) {
        val index = args.items?.correctIndex!!.toString()
        binding.enterQuestion.setText(items.question)
        binding.firstOption.setText(items.firstOption)
        binding.secondOption.setText(items.secondOption)
        binding.thirdOption.setText(items.thirdOption)
        binding.forthOption.setText(items.forthOption)
        binding.spinner.setSelection(getRegionIndex(index))
    }

    private fun clearAllInputs() {
        binding.enterQuestion.text.clear()
        binding.firstOption.text.clear()
        binding.secondOption.text.clear()
        binding.thirdOption.text.clear()
        binding.forthOption.text.clear()
    }

    private fun send() {
        //get all the inputs
        val question = binding.enterQuestion.text.toString().trim()
        val option1 = binding.firstOption.text.toString().trim()
        val option2 = binding.secondOption.text.toString().trim()
        val option3 = binding.thirdOption.text.toString().trim()
        val option4 = binding.forthOption.text.toString().trim()
        val correctOptionIndex = binding.spinner.selectedItem.toString().toInt()

        //check all the inputs
        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()) {
            Toast.makeText(activity, "Please complete the entries", Toast.LENGTH_LONG).show()
            return
        }

        val quiz = RoomEntity(
            question = question,
            firstOption = option1,
            image = null,
            secondOption = option2,
            thirdOption = option3,
            forthOption = option4,
            correctIndex = correctOptionIndex
        )
        //add to database
        if (args.items == null) {
            viewModel.addQuiz(quiz)
            requireActivity().showToast("Added")
        } else {
            quiz.id = args.items!!.id
            viewModel.updateQuiz(quiz)
            requireActivity().showToast("Updated")
            findNavController().navigate(R.id.savedQuizFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getRegionIndex(region: String): Int {
        val regionsInArray = resources.getStringArray(R.array.options)
        var index = 0
        for (i in regionsInArray.indices) {
            if (regionsInArray[i] == region) {
                index = i
            }
        }
        return index
    }
}