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
import com.maricoolsapps.adminpart.utils.MyServerDataState
import dagger.hilt.android.AndroidEntryPoint

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
                keyText.visibility = View.GONE
                key.visibility = View.GONE
                progressBar.visibility = View.GONE
                upload.visibility = View.GONE
                progressText.visibility = View.GONE
            }
        } else {
            binding.apply {
                errorMessage.visibility = View.GONE
                keyText.visibility = View.VISIBLE
                key.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                progressText.visibility = View.GONE
                upload.visibility = View.VISIBLE
            }
            server_quizModel = model.map()
        }

        binding.upload.setOnClickListener {
             key = binding.key.text.toString().trim()
            if (key.isEmpty()) {
                Toast.makeText(activity, "You inserted nothing", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                showAlert()
            }
        }
    }

    private fun showAlert() {
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setTitle("Notice")
        alertDialog.setMessage("Once you upload your quiz, you can't update it again")
        alertDialog.setPositiveButton("Yes") { _, _ ->
            addDataToServer(server_quizModel, key)
            }
        alertDialog.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun addDataToServer(data: List<Any>, key: String){

    binding.progressBar.visibility = View.VISIBLE
    binding.progressText.visibility = View.VISIBLE
    val size = data.size
    val progressIncrement = 100/size
    data.forEach {
        model.addToFirebase(key, it).observe(viewLifecycleOwner, Observer {result ->
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

}