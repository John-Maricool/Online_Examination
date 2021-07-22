package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.studentapp.databinding.FragmentQuizResultBinding
import com.maricoolsapps.utils.datastate.MyDataState
import com.maricoolsapps.utils.models.StudentUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizResultFragment : Fragment(R.layout.fragment_quiz_result) {

    private var _binding: FragmentQuizResultBinding? = null
    private val binding get() = _binding!!

    private val model: QuizResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentQuizResultBinding.bind(view)

        model.getStudent().observe(viewLifecycleOwner, { state->
            when(state){
                MyDataState.isLoading -> TODO()
                is MyDataState.notLoaded ->
                {binding.progressBar.visibility = View.GONE
                Toast.makeText(activity, "Check your Internet connection", Toast.LENGTH_SHORT).show()}
                is MyDataState.onLoaded -> updateView(state.data as StudentUser)
            }
        })
    }

    private fun updateView(studentUser: StudentUser) {
        binding.apply {
            progressBar.visibility = View.GONE
            textCongrats.append(studentUser.name)
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