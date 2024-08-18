package com.skilrock.retailapp.utils

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.TextView
import android.widget.Toast

fun Context.redToast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    val view = toast.view

    view.background.setColorFilter(Color.parseColor("#d30e24"), PorterDuff.Mode.SRC_IN)

    val text = view.findViewById<TextView>(android.R.id.message)
    text.setTextColor(Color.WHITE)
    toast.show()
}

fun Context.greenToast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
    val view = toast.view

    view.background.setColorFilter(Color.parseColor("#089148"), PorterDuff.Mode.SRC_IN)

    val text = view.findViewById<TextView>(android.R.id.message)
    text.setTextColor(Color.WHITE)
    toast.show()
}