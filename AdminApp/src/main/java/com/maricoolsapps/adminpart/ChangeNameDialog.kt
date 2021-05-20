package com.maricoolsapps.adminpart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_change_name_dialog.*
import javax.inject.Inject

@AndroidEntryPoint
class ChangeNameDialog : BottomSheetDialogFragment() {

   @Inject lateinit var auth:FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_name_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayName = auth.currentUser?.displayName
        name.setText(displayName)

        save.setOnClickListener {
            progress.visibility = View.VISIBLE
            val newName = name.text.toString()
            if (newName.isNotEmpty()) {
                val profile = UserProfileChangeRequest.Builder()
                        .setDisplayName(newName)
                        .build()
                auth.currentUser?.updateProfile(profile)?.addOnCompleteListener {
                    progress.visibility = View.GONE
                    if (it.isSuccessful) {
                        Toast.makeText(activity, "Name Changed", Toast.LENGTH_LONG).show()
                        dismiss()
                    } else {
                        Toast.makeText(activity, "Error Changing name", Toast.LENGTH_LONG).show()
                        dismiss()
                    }
                }
            }
        }

        cancel.setOnClickListener {
            dismiss()
        }
    }
}