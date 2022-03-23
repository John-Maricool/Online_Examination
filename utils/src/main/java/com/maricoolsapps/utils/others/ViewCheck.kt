package com.maricoolsapps.utils.others

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.maricoolsapps.utils.interfaces.AlertDialogListener
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

fun isTextValid(text: String): Boolean {
    return !(text.isEmpty() && text.length <= 6)
}

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
}

fun Activity.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun View.showSnack(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT)
        // .setBackgroundTint(resources.getColor(R.color.red, null))
        .show()
}

fun Activity.startAlertDialog(
    title: String,
    message: String? = null,
    view: EditText? = null,
    positiveListener: String = "Yes",
    negativeText: String = "Cancel",
    listener: AlertDialogListener
) {
    val builder = AlertDialog.Builder(this)
        .setTitle(title)
        .setPositiveButton(positiveListener) { dialog, _->
            val tex = view?.text.toString()
            listener.onPositiveListener(dialog, tex)
        }

        .setNegativeButton(negativeText) { dialog, _ ->
            val tex = view?.text.toString()
            listener.onNegativeListener(dialog, tex)
        }
    if(view != null){
        builder.setView(view)
    }
    if(message != null){
        builder.setMessage(message)
    }
    builder.show()
}

