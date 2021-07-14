package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.FragmentMainQuizBinding
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.others.constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainQuizFragment : Fragment(R.layout.fragment_main_quiz), MainQuizViewModel.onTimeClick {

    private var _binding: FragmentMainQuizBinding? = null
    val binding get() = _binding!!

    private val model: MainQuizViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainQuizBinding.bind(view)
        setNextButtonListener()
        model.questionsLive.observe(viewLifecycleOwner, {
            when (it) {
                is MyDataState.onLoaded -> {
                    loadQuestions()
                    model.startTimer(this)
                }
                MyDataState.isLoading -> TODO()
                is MyDataState.notLoaded -> Toast.makeText(activity, "Error Fetching questions", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadQuestions() {
        binding.optionsGroup.clearCheck()
        if (model.questionCount == model.questionsSize) {
            Log.i("MY", model.questionsSize.toString())
            binding.next.text = "Finish"
        }
        model.currentQuestion = model.questions?.get(model.questionIndex)
        binding.apply {
            questionNumberText.text = "Question ${model.questionCount} out of ${model.questionsSize}"
            question.text = model.currentQuestion?.question
            firstOption.text = model.currentQuestion?.firstOption
            secondOption.text = model.currentQuestion?.secondOption
            thirdOption.text = model.currentQuestion?.thirdOption
            forthOption.text = model.currentQuestion?.forthOption
        }
    }

    private fun setNextButtonListener() {
        binding.next.setOnClickListener {
            if (model.questionCount == model.questionsSize){
                checkIfCorrect()
                submit()
            }else{
                checkIfCorrect()
                model.questionIndex++
                model.questionCount++
                loadQuestions()
            }
        }
    }

    private fun submit() {
        convertResultToPercentageAndSend(model.score)
    }

    private fun convertResultToPercentageAndSend(score: Int) {
        binding.progressBar.visibility = View.VISIBLE
        val percentage = (score/model.questionsSize)*100
        model.sendQuizResult(percentage).observe(viewLifecycleOwner, { state->
            when(state){
                is MyServerDataState.isLoading -> TODO()
                is MyServerDataState.notLoaded -> {
                    Toast.makeText(activity, "Check your internet connection and try again", Toast.LENGTH_SHORT).show()
                }
                is MyServerDataState.onLoaded -> {
                    model.deactivateStudent().observe(viewLifecycleOwner, {inner_state->
                    when(inner_state){
                        MyServerDataState.isLoading -> TODO()
                        is MyServerDataState.notLoaded -> {
                            Toast.makeText(activity, "Check your internet connection and try again", Toast.LENGTH_SHORT).show()
                        }
                        MyServerDataState.onLoaded -> {
                            binding.progressBar.visibility = View.GONE
                            val action = MainQuizFragmentDirections.actionMainQuizToQuizResultFragment()
                            findNavController().navigate(action)
                        }
                    }

                    })
                }
            }
        })
    }

    private fun checkIfCorrect() {
            if (binding.firstOption.isChecked || binding.secondOption.isChecked || binding.thirdOption.isChecked || binding.forthOption.isChecked) {
                checkAnswer()
            } else {
                Toast.makeText(activity, "You Checked Nothing", Toast.LENGTH_SHORT).show()
                return
            }
    }

    private fun checkAnswer() {
        val id = binding.optionsGroup.findViewById<RadioButton>(binding.optionsGroup.checkedRadioButtonId)
        val answer = binding.optionsGroup.indexOfChild(id) + 1

        if (answer == model.currentQuestion?.correctIndex){
            model.score++
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        model.deleteQuiz()
        model.countDownTimer.cancel()
    }

    override fun onTickSelected(p0: Long, ttA: Int?) {
        updateTimerText(p0)
    }

    private fun updateTimerText(p0: Long) {
        val minutes = (p0 / 1000) /60
        val seconds = (p0 / 1000) % 60
        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding.timerText.text = timeFormatted
    }

    override fun onFinishSelected() {

    }
}