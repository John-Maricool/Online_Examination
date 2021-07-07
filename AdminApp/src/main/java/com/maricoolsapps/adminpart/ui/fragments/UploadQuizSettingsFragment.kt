package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.firebase.Timestamp
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.databinding.FragmentUploadQuizSettingsBinding
import com.maricoolsapps.adminpart.ui.viewModels.UploadQuizSettingViewModel
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.models.QuizSettingModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UploadQuizSettingsFragment : Fragment(R.layout.fragment_upload_quiz_settings) {

    private val model: UploadQuizSettingViewModel by viewModels()
    private var _binding: FragmentUploadQuizSettingsBinding? = null
    private val binding get() = _binding!!

    lateinit var list: Array<String>
    private val args: UploadQuizSettingsFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUploadQuizSettingsBinding.bind(view)
         list = arrayOf("5", "10", "15", "20")
        binding.numPicker.displayedValues = list

        binding.send.setOnClickListener {
            arrangeStuffs()
        }
    }

    private fun arrangeStuffs() {
        val day = binding.datePicker.dayOfMonth
        val year = binding.datePicker.year
        val month = binding.datePicker.dayOfMonth

        val hour = binding.tinePicker.hour
        val minute = binding.tinePicker.minute

        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        val date: Date = calendar.time

        Log.d("Quiz", date.toString())

        val quiz_timing = list[binding.numPicker.value].toInt()

        val stamp = Timestamp(date)
        val setting = QuizSettingModel(quiz_timing, stamp)
        clearDocsAndSend(setting)
    }

    private fun addSettingsToFirebase(setting: QuizSettingModel) {
        model.uploadQuizSettings(setting).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                true -> sendDataToFirebase()
                false -> Toast.makeText(activity, "Error sending data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendDataToFirebase() {
        val quiz = args.quiz.toList()

        val size = quiz.size
        val progressIncrement = 100 / size
        quiz.forEach {
            model.addToFirebase(it).observe(viewLifecycleOwner, androidx.lifecycle.Observer {state ->
                when(state) {
                    is MyServerDataState.onLoaded -> {
                        model.clicks++
                        if (model.clicks == size) {
                            binding.progressUpload.visibility = View.GONE
                            binding.progressText.visibility = View.GONE
                            Toast.makeText(activity, "Upload Successful", Toast.LENGTH_LONG).show()
                        } else {
                            binding.progressUpload.progress += progressIncrement
                            binding.progressText.text = "${model.clicks}/$size"
                        }
                    }
                is MyServerDataState.notLoaded -> {
                    binding.progressUpload.visibility = View.GONE
                    binding.progressText.visibility = View.GONE
                    Toast.makeText(activity, "Upload Failed", Toast.LENGTH_LONG).show()
                }
                    is MyServerDataState.isLoading -> TODO()
                }
            })
        }
    }

    private fun clearDocsAndSend(setting: QuizSettingModel) {
        binding.progressUpload.visibility = View.VISIBLE
        binding.progressText.visibility = View.VISIBLE
        model.clearQuizDocs().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it){
                true -> addSettingsToFirebase(setting)
                false -> {
                    binding.progressUpload.visibility = View.GONE
                    binding.progressText.visibility = View.GONE
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()}
                null -> sendDataToFirebase()
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}