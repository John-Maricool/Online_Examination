package com.maricoolsapps.utils.interfaces

import android.content.DialogInterface

interface AlertDialogListener {
    fun onPositiveListener(dialog: DialogInterface, text: String = "")
    fun onNegativeListener(dialog: DialogInterface, text: String = "")
}