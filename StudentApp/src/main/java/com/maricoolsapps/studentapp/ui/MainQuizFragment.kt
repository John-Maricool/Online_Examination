package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.maricoolsapps.room_library.room.RoomEntity
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.FragmentMainQuizBinding
import com.maricoolsapps.utils.datastate.MyDataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainQuizFragment : Fragment(R.layout.fragment_main_quiz) {

    private var _binding: FragmentMainQuizBinding? = null
    val binding get() = _binding!!

    private val model: MainQuizViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainQuizBinding.bind(view)

        model.questionsLive.observe(viewLifecycleOwner, {
            when (it) {
                is MyDataState.onLoaded -> {
                    val result = it.data as List<RoomEntity>
                    model.questions = result
                    model.questionsSize = result.size
                    Log.i("MY", model.questionsSize.toString())
                    loadQuestions()
                }
                MyDataState.isLoading -> TODO()
                is MyDataState.notLoaded -> Toast.makeText(activity, "Error Fetching questions", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        binding.next.setOnClickListener {
            if (model.questionCount == model.questionsSize){
                submit()
            }else{
                model.questionIndex++
                model.questionCount++
                checkIfCorrect()
                loadQuestions()
            }
        }
    }

    private fun submit() {

    }

    private fun checkIfCorrect() {
        val id = binding.optionsGroup.checkedRadioButtonId
        if (id == -1){
            model.isAnswered = false
        }else{
            model.isAnswered = true
            if (id == model.currentQuestion?.correctIndex){
                model.score++
            }
        }
        Log.i("MY", id.toString())
        binding.optionsGroup.clearCheck()
    }

    private fun loadQuestions() {
        model.isAnswered = false
        if (model.questionCount == model.questionsSize) {
            Log.i("MY", model.questionsSize.toString())
            binding.next.text = "Finish"
        }
            model.currentQuestion = model.questions?.get(model.questionIndex)
            binding.apply {
                question.text = model.currentQuestion?.question
                firstOption.text = model.currentQuestion?.firstOption
                secondOption.text = model.currentQuestion?.secondOption
                thirdOption.text = model.currentQuestion?.thirdOption
                forthOption.text = model.currentQuestion?.forthOption
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        model.deleteQuiz()
    }
}