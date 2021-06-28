package com.maricoolsapps.adminpart.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.ui.viewModels.UploadQuizViewModel
import com.maricoolsapps.adminpart.databinding.FragmentUploadQuizBinding
import com.maricoolsapps.room_library.room.ServerQuizDataModel
import com.maricoolsapps.utils.datastate.MyServerDataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_quiz_arrangement.*

@AndroidEntryPoint
class UploadQuizFragment : Fragment(R.layout.fragment_upload_quiz) {

    private val model: UploadQuizViewModel by viewModels()

    private var _binding: FragmentUploadQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var server_quizModel: List<ServerQuizDataModel>

    lateinit var key: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUploadQuizBinding.bind(view)

        if (model.isQuizEmpty()) {
            binding.apply {
                errorMessage.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                upload.visibility = View.GONE
                progressText.visibility = View.GONE
            }
        } else {
            binding.apply {
                errorMessage.visibility = View.GONE
                progressBar.visibility = View.GONE
                progressText.visibility = View.GONE
                upload.visibility = View.VISIBLE
            }
            server_quizModel = model.map()
        }

        binding.upload.setOnClickListener {
            addDataToServer(server_quizModel)
        }

        binding.overWrite.setOnClickListener{
            deleteQuizOnline()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun addDataToServer(data: List<Any>){

    binding.progressBar.visibility = View.VISIBLE
    binding.progressText.visibility = View.VISIBLE
    val size = data.size
    val progressIncrement = 100/size
    data.forEach {
        model.addToFirebase(it).observe(viewLifecycleOwner, Observer {result ->
            when(result) {
                is MyServerDataState.onLoaded -> {
                    model.clicks++
                    if (model.clicks == size){
                        binding.progressBar.visibility = View.GONE
                        binding.progressText.visibility = View.GONE
                        Toast.makeText(activity, "Upload Successful", Toast.LENGTH_LONG).show()
                    }
                    else{
                        binding.progressBar.progress += progressIncrement
                        binding.progressText.text = "${model.clicks}/$size"
                    }
                }

                is MyServerDataState.notLoaded -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressText.visibility = View.GONE
                    Toast.makeText(activity, "Upload Failed", Toast.LENGTH_LONG).show()
                }
                MyServerDataState.isLoading -> TODO()
            }

        })
        }
    }

    private fun deleteQuizOnline(){
        binding.progressBar.visibility = View.VISIBLE
        binding.progressText.visibility = View.VISIBLE
        model.deleteQuizDocs.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    addDataToServer(server_quizModel)
                }
                false -> {
                    Toast.makeText(activity, "Check Your internet connection", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}