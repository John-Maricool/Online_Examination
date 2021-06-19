package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.ui.viewModels.ChangeNameViewModel
import com.maricoolsapps.utils.MyServerDataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_change_name_dialog.*

@AndroidEntryPoint
class ChangeNameDialog : BottomSheetDialogFragment() {

    private val model: ChangeNameViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_name_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayName = model.name
        name.setText(displayName)

        save.setOnClickListener {
            progress.visibility = View.VISIBLE
            val newName = name.text.toString()
            if (newName.isNotEmpty()) {
                model.changeName(newName).observe(viewLifecycleOwner, Observer { result ->
                    when (result) {
                        is com.maricoolsapps.utils.MyServerDataState.onLoaded -> {
                            progress.visibility = View.GONE
                            Toast.makeText(activity, "Name Changed", Toast.LENGTH_LONG).show()
                            dismiss()
                        }
                        is com.maricoolsapps.utils.MyServerDataState.notLoaded -> {
                            progress.visibility = View.GONE
                            Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
                        }
                        com.maricoolsapps.utils.MyServerDataState.isLoading -> TODO()
                    }
                })
            }
        }

        cancel.setOnClickListener {
            dismiss()
        }
    }
}