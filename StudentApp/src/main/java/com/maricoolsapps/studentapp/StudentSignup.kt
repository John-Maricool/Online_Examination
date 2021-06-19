package com.maricoolsapps.studentapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.maricoolsapps.studentapp.databinding.FragmentStudentSignupBinding

class StudentSignup : Fragment(R.layout.fragment_student_signup) {

    private var _binding: FragmentStudentSignupBinding? = null
    val binding: FragmentStudentSignupBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStudentSignupBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}