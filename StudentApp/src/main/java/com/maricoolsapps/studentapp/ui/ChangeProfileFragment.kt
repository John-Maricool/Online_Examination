package com.maricoolsapps.studentapp.ui

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maricoolsapps.resources.databinding.ChangeProfileLayoutBinding
import com.maricoolsapps.studentapp.R
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.others.constants
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_change_profile.*

@AndroidEntryPoint
class ChangeProfileFragment : BottomSheetDialogFragment() {

    private val model: ChangeProfileViewModel by viewModels()
    private val args: ChangeProfileFragmentArgs by navArgs()

    private var _binding: ChangeProfileLayoutBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ChangeProfileLayoutBinding.bind(view)
        binding.textView.append(" Name")
        binding.name.setText(args.name)
        buttonClicks()
        observeLiveData()
    }

    private fun observeLiveData() {
        model.state.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    requireActivity().showToast(result.data!!)
                    dismiss()
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    binding.progress.showSnack(result.message!!)
                }
                Status.LOADING -> binding.progress.visibility = View.GONE
            }
        }
    }

    private fun buttonClicks() {
        binding.cancel.setOnClickListener {
            dismiss()
        }
        binding.save.setOnClickListener {
            val new = binding.name.text.toString().trim()
            if (new.isNotEmpty()) {
                model.changeName(new)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}