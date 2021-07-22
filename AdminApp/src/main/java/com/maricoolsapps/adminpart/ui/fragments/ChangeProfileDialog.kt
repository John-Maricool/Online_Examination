package com.maricoolsapps.adminpart.ui.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.maricoolsapps.adminpart.R
import com.maricoolsapps.adminpart.ui.viewModels.ChangeProfileDialogViewModel
import com.maricoolsapps.utils.datastate.MyServerDataState
import com.maricoolsapps.utils.others.constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_change_name_dialog.*
import java.util.regex.Matcher

@AndroidEntryPoint
class ChangeProfileDialog : BottomSheetDialogFragment() {

    private val model: ChangeProfileDialogViewModel by viewModels()

    private val args: ChangeProfileDialogArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_name_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.type == constants.username){
            textView.append("Name")
            val displayName = model.name
            name.setText(displayName)
        }else{
            textView.append("Email")
            val displayMail = model.email
            name.setText(displayMail)
        }

        save.setOnClickListener {
            progress.visibility = View.VISIBLE
            val new = name.text.toString().trim()
            if (new.isNotEmpty() && args.type == constants.username) {
                model.changeName(new).observe(viewLifecycleOwner, { result ->
                    when (result) {
                        is MyServerDataState.onLoaded -> {
                            progress.visibility = View.GONE
                            Toast.makeText(activity, "Successfully Changed", Toast.LENGTH_LONG).show()
                            dismiss()
                        }
                        is MyServerDataState.notLoaded -> {
                            progress.visibility = View.GONE
                            Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
                        }
                        MyServerDataState.isLoading -> TODO()
                    }
                })
            }
            else if (new.isNotEmpty() && args.type == constants.mail && Patterns.EMAIL_ADDRESS.matcher(new).matches()){
                model.changeMail(new).observe(viewLifecycleOwner, { result ->
                    when (result) {
                        is MyServerDataState.onLoaded -> {
                            progress.visibility = View.GONE
                            Toast.makeText(activity, "Successfully Changed", Toast.LENGTH_LONG).show()
                            dismiss()
                        }
                        is MyServerDataState.notLoaded -> {
                            progress.visibility = View.GONE
                            Toast.makeText(activity, it.toString(), Toast.LENGTH_LONG).show()
                        }
                        MyServerDataState.isLoading -> TODO()
                    }
                })
            }else{
                Toast.makeText(activity, "Error with your entries", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }

        cancel.setOnClickListener {
            dismiss()
        }
    }
}