package com.maricoolsapps.studentapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.maricoolsapps.studentapp.databinding.FragmentMainBinding

class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    val binding: FragmentMainBinding get() = _binding!!

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            _binding = FragmentMainBinding.bind(view)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}