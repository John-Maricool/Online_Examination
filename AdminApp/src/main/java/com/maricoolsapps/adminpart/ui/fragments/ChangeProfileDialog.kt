package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.ui.viewModels.ChangeProfileDialogViewModel
import com.maricoolsapps.resources.databinding.ChangeProfileLayoutBinding
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.others.Status
import com.maricoolsapps.utils.others.constants
import com.maricoolsapps.utils.others.showSnack
import com.maricoolsapps.utils.others.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_change_name_dialog.*

@AndroidEntryPoint
class ChangeProfileDialog : BottomSheetDialogFragment() {

    private val model: ChangeProfileDialogViewModel by viewModels()
    private val args: ChangeProfileDialogArgs by navArgs()
    private var _binding: ChangeProfileLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_name_dialog, container, false)
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