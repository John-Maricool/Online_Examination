package com.maricoolsapps.utils.others

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream

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

fun Activity.encodeImage(img: String): String {

    val bm = BitmapFactory.decodeStream(this.contentResolver.openInputStream(img.toUri()))

    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

/*
fun ImageView.setResource(uri: String){
    Glide.with(context)
        .load(uri)
        .centerCrop()
        .placeholder(R.drawable.profile)
        .into(profileImage)
}*/
