package com.maricoolsapps.utils.others

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

fun isTextValid(text: String): Boolean{
    return !(text.isEmpty() && text.length <= 6)
}

fun isEmailValid(email: String): Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()
}

fun Activity.showToast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun View.showSnack(msg: String){
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT)
       // .setBackgroundTint(resources.getColor(R.color.red, null))
        .show()
}

fun Activity.encodeImage(uri: Uri): String {
    val iStream: InputStream? = contentResolver.openInputStream(uri)
    val inputData = getBytes(iStream!!)
    return String(inputData!!)
}

@Throws(IOException::class)
fun getBytes(inputStream: InputStream): ByteArray? {
    val byteBuffer = ByteArrayOutputStream()
    val bufferSize = 1024
    val buffer = ByteArray(bufferSize)
    var len = 0
    while (inputStream.read(buffer).also { len = it } != -1) {
        byteBuffer.write(buffer, 0, len)
    }
    return byteBuffer.toByteArray()
}

/*
fun ImageView.setResource(uri: String){
    Glide.with(context)
        .load(uri)
        .centerCrop()
        .placeholder(R.drawable.profile)
        .into(profileImage)
}*/
