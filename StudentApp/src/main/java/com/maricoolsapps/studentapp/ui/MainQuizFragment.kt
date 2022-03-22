package com.maricoolsapps.studentapp.ui

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.FragmentMainQuizBinding
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.interfaces.AlertDialogListener
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.others.constants
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.startAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainQuizFragment : Fragment(R.layout.fragment_main_quiz),
    MainQuizViewModel.onTimeClick, AlertDialogListener {

    private var _binding: FragmentMainQuizBinding? = null
    val binding get() = _binding!!
    lateinit var currentQuestion: ServerQuizDataModel
    lateinit var quizzesId: List<String>
    private val model: MainQuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainQuizBinding.bind(view)
        observeLiveData()
        setNextButtonListener()
    }

    private fun showDialog() {
        requireActivity().startAlertDialog(title = "Exit Quiz", listener = this)
        /*val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Exit Quiz")

        builder.setPositiveButton("Yes") { dialog, _ ->
            binding.all.isEnabled = false
            dialog.dismiss()
            findNavController().popBackStack(R.id.mainFragment, true)
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()*/
    }

    private fun loadQuestions() {
        if (model.questionCount == quizzesId.size) {
            binding.next.text = "Finish"
        }
        binding.optionsGroup.clearCheck()
        model.getQuestion(quizzesId[model.questionIndex])
    }

    private fun setNextButtonListener() {
        binding.next.setOnClickListener {
            binding.all.isEnabled = false
            if (model.questionCount == quizzesId.size) {
                checkIfCorrect()
                model.sendQuizResult()
            } else {
                checkIfCorrect()
                model.questionIndex++
                model.questionCount++
                loadQuestions()
            }
        }
    }

    private fun checkIfCorrect() {
        if (binding.firstOption.isChecked || binding.secondOption.isChecked || binding.thirdOption.isChecked
            || binding.forthOption.isChecked) {
            checkAnswer()
        } else {
            Toast.makeText(activity, "You Checked Nothing", Toast.LENGTH_SHORT).show()
            return
        }
    }

    private fun checkAnswer() {
        val id =
            binding.optionsGroup.findViewById<RadioButton>(binding.optionsGroup.checkedRadioButtonId)
        val answer = binding.optionsGroup.indexOfChild(id) + 1
        if (answer == currentQuestion.correctIndex) {
            model.score++
            Log.d("SCORE", model.score.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        model.countDownTimer?.cancel()
    }

    override fun onTickSelected(p0: Long, ttA: Int?) {
        updateTimerText(p0)
    }

    private fun updateTimerText(p0: Long) {
        val minutes = (p0 / 1000) / 60
        val seconds = (p0 / 1000) % 60
        val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
        binding.timerText.text = timeFormatted
    }

    override fun onFinishSelected() {
        binding.next.callOnClick()
    }

    private fun LoadNewQuestion() {
        model.question.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it?.data != null) {
                        binding.all.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                        currentQuestion = it.data!!.toObject(ServerQuizDataModel::class.java)!!
                        binding.apply {
                            questionNumberText.text =
                                "Question ${model.questionCount} over ${quizzesId.size}"
                            question.text = currentQuestion.question
                            if (currentQuestion.image != null) {
                                binding.image.visibility = View.VISIBLE
                                Glide.with(requireActivity())
                                    .load(currentQuestion.image)
                                    .centerCrop()
                                    .into(image)
                            } else {
                                binding.image.visibility = View.GONE
                            }
                            firstOption.text = currentQuestion.firstOption
                            secondOption.text = currentQuestion.secondOption
                            thirdOption.text = currentQuestion.thirdOption
                            forthOption.text = currentQuestion.forthOption
                        }
                        model.countDownTimer?.cancel()
                        model.startTimer(this)
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBar.showSnack(it.toString())
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun observeLiveData() {
        model.questionsLive.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        quizzesId = it.data!!
                        model.getQuestion(quizzesId[model.questionIndex])
                        binding.progressBar.visibility = View.GONE
                    } else {
                        binding.progressBar.showSnack("No data")
                    }
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBar.showSnack("check your internet connection and try again")
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
        LoadNewQuestion()
        finishUp()
    }

    private fun finishUp() {
        model.finish.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(R.id.quizResultFragment)
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBar.showSnack("check your internet connection and try again")
                }
                Status.LOADING -> binding.progressBar.visibility = View.VISIBLE

            }
        }
    }

    override fun onPositiveListener(dialog: DialogInterface, text: String) {
        binding.all.isEnabled = false
        dialog.dismiss()
        findNavController().popBackStack(R.id.mainFragment, true)
    }

    override fun onNegativeListener(dialog: DialogInterface, text: String) {
        dialog.cancel()
    }
}