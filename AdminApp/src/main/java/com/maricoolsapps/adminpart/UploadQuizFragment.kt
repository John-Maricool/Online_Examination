package com.maricoolsapps.adminpart

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.maricoolsapps.adminpart.databinding.FragmentAdminProtalBinding
import com.maricoolsapps.adminpart.databinding.FragmentUploadQuizBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UploadQuizFragment : Fragment(R.layout.fragment_upload_quiz) {

    private val model: UploadQuizViewModel by viewModels()
    @Inject
    lateinit var cloud: FirebaseFirestore
    @Inject
    lateinit var auth: FirebaseAuth

    private var _binding: FragmentUploadQuizBinding? = null
    private val binding get() = _binding!!

    lateinit var server_quiz: List<ServerQuizData>

    lateinit var key: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentUploadQuizBinding.bind(view)

        if (model.isQuizEmpty() == true) {
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
            server_quiz = model.map()
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
        alertDialog.setPositiveButton("Yes", object: DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                sendToCloud()
            }
        })
        alertDialog.create().show()
    }

    private fun sendToCloud() {
        binding.progressBar.visibility = View.VISIBLE
        val col = cloud.collection("Admins").document(auth.currentUser.uid).collection(key)
        val countProgress = 0
        var counter = 0
        val size = server_quiz.size
        val increment = 100 / size

        server_quiz.forEach { quiz ->
            binding.progressText.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
            col.add(quiz).addOnSuccessListener {
                counter++
                if (counter == size) {
                    Toast.makeText(activity, "Upload Successful", Toast.LENGTH_LONG).show()
                    model.deleteQuiz()
                    binding.progressText.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                } else {
                    binding.progressBar.progress = countProgress + increment
                    binding.progressText.text = "$counter/$size"
                }
            }.addOnFailureListener {
                Toast.makeText(activity, "Failed to Upload", Toast.LENGTH_LONG).show()
                binding.progressText.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}