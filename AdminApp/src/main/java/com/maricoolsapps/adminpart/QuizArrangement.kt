package com.maricoolsapps.adminpart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.maricoolsapps.adminpart.databinding.FragmentAdminProtalBinding
import com.maricoolsapps.adminpart.databinding.FragmentQuizArrangementBinding
import com.maricoolsapps.adminpart.room.RoomEntity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuizArrangement : Fragment(R.layout.fragment_quiz_arrangement) {

    private var _binding: FragmentQuizArrangementBinding? = null
    private val binding get() = _binding!!

   private val viewModel: QuizArrangementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizArrangementBinding.bind(view)


        binding.add.setOnClickListener {
            send()
            clearAllInputs()
        }

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
        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()){
            Toast.makeText(activity, "Please complete the entries", Toast. LENGTH_LONG).show()
            return
        }
        //create the class
        val quiz = RoomEntity(question = question,
                firstOption = option1,
                secondOption = option2,
                thirdOption = option3,
                forthOption = option4,
                correctIndex = correctOptionIndex)
        //add to database
        viewModel.addQuiz(quiz)
        Toast.makeText(activity, "Added", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}